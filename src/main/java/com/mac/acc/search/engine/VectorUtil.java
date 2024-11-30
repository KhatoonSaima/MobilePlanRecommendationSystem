package com.mac.acc.search.engine;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Utility class providing vector operations for search result calculations.
 * Implements core mathematical operations needed for combining search results
 * and computing relevance scores.
 * @author Weiming Zheng
 * @since 2024-11-30
 */
@Slf4j
public class VectorUtil {
    /**
     * Adds two vectors element-wise.
     * Used for combining search results from different fields or conditions.
     */
    public static List<Integer> plus(List<Integer> vector1, List<Integer> vector2) {
        if (vector1 == null) return vector2;
        if (vector2 == null) return vector1;
        assert vector1.size() == vector2.size();

        log.debug("Adding vectors of size {}", vector1.size());
        return IntStream.range(0, vector1.size())
                .mapToObj(i -> vector1.get(i) + vector2.get(i))
                .toList();
    }

    /**
     * Multiplies two vectors element-wise.
     * Critical for applying mask vectors and combining search conditions.
     */
    public static List<Integer> multiply(List<Integer> vector1, List<Integer> vector2) {
        if (vector1 == null) return vector2;
        if (vector2 == null) return vector1;
        assert vector1.size() == vector2.size();

        log.debug("Multiplying vectors of size {}", vector1.size());
        return IntStream.range(0, vector1.size())
                .mapToObj(i -> vector1.get(i) * vector2.get(i))
                .toList();
    }

    /**
     * Computes a mask vector from multiple vectors.
     * Used to enforce tight field conditions by marking documents
     * that contain all required words.
     */
    public static List<Integer> computeMaskVector(List<List<Integer>> vectors) {
        if (vectors == null) return null;
        if (vectors.isEmpty()) return new ArrayList<>();

        log.debug("Computing mask vector for {} input vectors", vectors.size());
        List<Integer> result = new ArrayList<>(Collections.nCopies(vectors.getFirst().size(), 1));

        for (int i = 0; i < result.size(); ++i) {
            for (List<Integer> vector : vectors) {
                if (vector.get(i) == 0) {
                    result.set(i, 0);
                    break;
                }
            }
        }

        log.debug("Mask vector computed with {} elements", result.size());
        return result;
    }

    /**
     * Computes the sum of all vectors (sigma operation).
     * Used for combining word frequencies within fields.
     */
    public static List<Integer> sigma(List<List<Integer>> vectors) {
        if (vectors == null) return null;
        if (vectors.isEmpty()) return new ArrayList<>();

        log.debug("Computing sigma for {} vectors", vectors.size());
        List<Integer> identity = new ArrayList<>(Collections.nCopies(vectors.getFirst().size(), 0));
        return vectors.stream().reduce(identity, VectorUtil::plus);
    }

    /**
     * Computes the product of all vectors (pi operation).
     * Used for combining mask vectors from multiple tight fields.
     */
    public static List<Integer> pi(List<List<Integer>> vectors) {
        if (vectors == null) return null;
        if (vectors.isEmpty()) return new ArrayList<>();

        log.debug("Computing pi for {} vectors", vectors.size());
        List<Integer> identity = new ArrayList<>(Collections.nCopies(vectors.getFirst().size(), 1));
        return vectors.stream().reduce(identity, VectorUtil::multiply);
    }
}