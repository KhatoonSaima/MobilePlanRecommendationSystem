package com.mac.acc.search.engine;

import com.mac.acc.search.Document;
import com.mac.acc.search.Field;
import com.mac.acc.search.engine.exception.InvalidSearchFieldException;
import com.mac.acc.search.engine.exception.SearchEngineException;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.IntStream;

/**
 * Implementation of the SearchEngine interface specialized for mobile data plan search.
 * Provides advanced search capabilities with support for tight and loose field conditions.
 *
 * @author Weiming Zheng
 * @since 2024-11-30
 */
@Slf4j
public class MobileDataPlanSearchEngine implements SearchEngine {
    private final List<Document> documents;
    private final Trie rawCaseInvertedIndex;
    private final Trie lowerCaseInvertedIndex;
    private static final int MAX_FIELDS = 5;

    public MobileDataPlanSearchEngine(List<Document> documents) {
        validateDocuments(documents);

        log.info("Initializing MobileDataPlanSearchEngine with {} documents", documents.size());
        this.documents = new ArrayList<>(documents); // Defensive copy
        this.rawCaseInvertedIndex = new InvertedIndex(documents.size());
        this.lowerCaseInvertedIndex = new InvertedIndex(documents.size());

        try {
            buildIndices();
        } catch (Exception e) {
            throw new SearchEngineException("Failed to build search indices", e);
        }
    }

    private void validateDocuments(List<Document> documents) {
        if (documents == null || documents.isEmpty()) {
            throw new SearchEngineException("Document list cannot be null or empty");
        }

        // Check for duplicate documents
        Set<Document> uniqueDocs = new HashSet<>(documents);
        if (uniqueDocs.size() != documents.size()) {
            throw new SearchEngineException("Duplicate documents detected in input");
        }

        // Validate individual documents
        documents.forEach(doc -> {
            if (doc == null) {
                throw new SearchEngineException("Null document detected in input");
            }
            if (doc.getPath() == null || doc.getUrl() == null) {
                throw new SearchEngineException(
                        "Invalid document detected: path and url must not be null");
            }
        });
    }

    private void buildIndices() {
        IntStream.range(0, documents.size()).forEach(i -> {
            List<String> words = documents.get(i).extractWords();
            log.debug("Indexing document {}: {} words extracted", i, words.size());

            if (words.isEmpty()) {
                log.warn("Document {} contains no indexable words", documents.get(i).getUrl());
            }

            words.forEach(word -> {
                try {
                    rawCaseInvertedIndex.insert(word, i);
                    lowerCaseInvertedIndex.insert(word.toLowerCase(), i);
                } catch (Exception e) {
                    throw new SearchEngineException(
                            String.format("Failed to index word '%s' for document %d", word, i), e);
                }
            });
        });
    }

    @Override
    public List<Entry<String, Integer>> search(List<Field> fields) {
        if (fields == null || fields.isEmpty()) return new ArrayList<>();

        // Validate search fields
        if (fields.size() > MAX_FIELDS) {
            throw new InvalidSearchFieldException(
                    String.format("Too many search fields. Maximum %d fields allowed", MAX_FIELDS));
        }

        log.info("Processing search with {} fields", fields.size());

        try {
            // Step 1: Generate word-level vectors for each field
            List<List<List<Integer>>> regularVectorsInFields = fields.stream()
                    .peek(field -> {
                        if (!field.isCaseSensitive()) {
                            String original = field.getValue();
                            field.setValue(field.getValue().toLowerCase());
                            log.debug("Converting field '{}' to lowercase: '{}'", original, field.getValue());
                        }
                    })
                    .map(field -> {
                        log.debug("Processing field: value='{}', scope={}, caseSensitive={}",
                                field.getValue(), field.getScope(), field.isCaseSensitive());

                        // Split field value into words and get frequency vectors
                        List<List<Integer>> wordVectors = Arrays.stream(field.getValue().split(" "))
                                .map(word -> {
                                    List<Integer> vector = field.isCaseSensitive() ?
                                            rawCaseInvertedIndex.query(word) :
                                            lowerCaseInvertedIndex.query(word);
                                    log.trace("Word '{}' vector generated", word);
                                    return vector;
                                })
                                .toList();

                        log.debug("Field '{}' generated {} word vectors", field.getValue(), wordVectors.size());
                        return wordVectors;
                    }).toList();

            // Step 2: Generate mask vectors to enforce tight field conditions
            List<List<Integer>> maskVectors = regularVectorsInFields.stream()
                    .map(vectors -> {
                        List<Integer> mask = VectorUtil.computeMaskVector(vectors);
                        log.debug("Mask vector computed with {} elements", mask.size());
                        return mask;
                    })
                    .toList();

            // Step 3: Calculate field-level vectors combining word frequencies and masks
            List<List<Integer>> fieldVectors = IntStream.range(0, fields.size())
                    .mapToObj(i -> {
                        List<List<Integer>> regularVectors = regularVectorsInFields.get(i);
                        List<Integer> maskVector = maskVectors.get(i);
                        List<Integer> fieldVector = VectorUtil.multiply(
                                VectorUtil.sigma(regularVectors),
                                maskVector
                        );
                        log.debug("Field {} vector computed", i);
                        return fieldVector;
                    }).toList();

            // Step 4: Process tight condition fields - all words must appear
            List<Integer> tightVectorResult = IntStream.range(0, fields.size())
                    .mapToObj(i -> {
                        if (fields.get(i).getScope().equals(FieldCondition.TIGHT)) {
                            log.debug("Processing tight field {}", i);
                            return fieldVectors.get(i);
                        }
                        return null;
                    })
                    .filter(Objects::nonNull)
                    .reduce(new ArrayList<>(Collections.nCopies(documents.size(), 0)), VectorUtil::plus);

            // Step 5: Process loose condition fields - any matching word increases relevance
            List<Integer> looseVectorResult = IntStream.range(0, fields.size())
                    .mapToObj(i -> {
                        if (fields.get(i).getScope().equals(FieldCondition.LOOSE)) {
                            log.debug("Processing loose field {}", i);
                            return fieldVectors.get(i);
                        }
                        return null;
                    })
                    .filter(Objects::nonNull)
                    .reduce(new ArrayList<>(Collections.nCopies(documents.size(), 0)), VectorUtil::plus);

            // Step 6: Generate final mask for tight conditions
            List<Integer> tightMaskVectorResult = IntStream.range(0, fields.size())
                    .mapToObj(i -> {
                        if (fields.get(i).getScope().equals(FieldCondition.TIGHT)) {
                            log.debug("Including mask vector for tight field {}", i);
                            return maskVectors.get(i);
                        }
                        return null;
                    })
                    .filter(Objects::nonNull)
                    .reduce(new ArrayList<>(Collections.nCopies(documents.size(), 1)), VectorUtil::multiply);

            // Step 7: Combine results: (tight_results * tight_mask) + loose_results
            List<Integer> finalResults = VectorUtil.plus(
                    VectorUtil.multiply(tightVectorResult, tightMaskVectorResult),
                    looseVectorResult
            );
            log.info("Search computation complete, ranking results");

            // Step 8: Create ranked result list
            return IntStream.range(0, documents.size())
                    .mapToObj(i -> {
                        Entry<String, Integer> entry = new AbstractMap.SimpleImmutableEntry<>(
                                documents.get(i).getUrl(),
                                finalResults.get(i));
                        log.debug("Document {} score: {}", documents.get(i).getUrl(), finalResults.get(i));
                        return entry;
                    })
                    .filter(entry -> entry.getValue() > 0)
                    .sorted(Comparator.<Entry<String, Integer>>comparingInt(Entry::getValue).reversed())
                    .toList();

        } catch (Exception e) {
            throw new SearchEngineException("Error during search operation", e);
        }
    }

}
