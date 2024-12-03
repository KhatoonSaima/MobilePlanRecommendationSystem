package com.mac.acc.features;
import java.io.*;
import java.util.*;


public class SpellChecker {

        // class to represent each node in the trie
        static class TrieNode {
            TrieNode[] children = new TrieNode[26]; // we have 26 children nodes to hold all letters (a-z) in the alphabet
            boolean isEndofWord = false; // when true this will mark the end of the current word
        }

        // class to manage the Trie structure, handles insertion and search
        static class Trie {
            private TrieNode root; // this is the root node of the trie this is always empty

            // constructor for the root node
            public Trie() {
                root = new TrieNode(); // initializes the root node of the trie
            }

            // method selects a word and inserts it into the trie, one character at a time
            public void insertWord(String word) {
                TrieNode currNode = root;
                // use .toCharArray to turn word to an array of characters
                for (char c : word.toCharArray()) {
                    if (c < 'a' || c > 'z') continue; // skip any characters that are not in the range a-z
                    int index = c - 'a'; //using -'a' converts char to index (0-25)
                    if (currNode.children[index] == null) {
                        currNode.children[index] = new TrieNode();
                    }
                    currNode = currNode.children[index];
                }
                currNode.isEndofWord = true; // this signifies the end of the word
            }

            // this method crosschecks if a word already exists in the trie or not
            public boolean searchWord(String word) {
                TrieNode currNode = root;
                // use .toCharArray to turn word to an array of characters
                for (char c : word.toCharArray()) {
                    if (c < 'a' || c > 'z') {
                        continue; // skip any characters that are not in the range a-z
                    }
                    int index = c - 'a'; // using -'a' converts char to index (0-25)
                    if (currNode.children[index] == null) {
                        return false;
                    }
                    currNode = currNode.children[index];
                }
                return currNode.isEndofWord; // check if this is the end of the word
            }

            // method used to calculate the editDistance
            public static int computeEditDistance(String wordOne, String wordTwo) {
                int wordOneLen = wordOne.length(); // first word len
                int wordTwolen = wordTwo.length(); // secornd word len
                int[][] dp = new int[wordOneLen + 1][wordTwolen + 1]; // 2d array used for dynamic programming

                // fill the 2D array
                for (int i = 0; i <= wordOneLen; i++) {
                    for (int j = 0; j <= wordTwolen; j++) {
                        if (i == 0) {
                            dp[i][j] = j; // If word one is empty
                        } else if (j == 0) {
                            dp[i][j] = i; // If word two is empty
                        } else {
                            // Calculate minimum cost for insertion, deletion, or substitution
                            dp[i][j] = Math.min(dp[i - 1][j] + 1, // Deletion
                                    Math.min(dp[i][j - 1] + 1, // Insertion
                                            dp[i - 1][j - 1] + (wordOne.charAt(i - 1) == wordTwo.charAt(j - 1) ? 0 : 1))); // Substitution
                        }
                    }
                }
                return dp[wordOneLen][wordTwolen]; // returns the edit distance
            }

             //this method suggest similar words based on edit distance
             public static List<String> suggestSimilarWords(Trie dictionary, String word, int maxSuggestions) {
                 PriorityQueue<Map.Entry<String, Integer>> suggestions = new PriorityQueue<>(
                         Comparator.comparingInt(Map.Entry::getValue) // Sort by edit distance
                 );
                 suggestSimilarWordsHelper(dictionary.root, "", suggestions, word);

                 // Extract top suggestions
                 List<String> altWords = new ArrayList<>();
                 while (!suggestions.isEmpty() && altWords.size() < maxSuggestions) {
                     altWords.add(suggestions.poll().getKey());
                 }
                 return altWords;
             }

            //this is a helper functyion that will generate suggestions using trie (recursive depth first search approach)
            private static void suggestSimilarWordsHelper(TrieNode node, String currentWord, PriorityQueue<Map.Entry<String, Integer>> suggestions, String targetWord) {
                if (node == null) return;  // Stop if no node exists

                // If the current word is complete, compute edit distance
                if (node.isEndofWord) {
                    int wordEditDistance = computeEditDistance(currentWord, targetWord);
                    suggestions.add(Map.entry(currentWord, wordEditDistance)); // Add word with its edit distance
                }

                // Continue DFS traversal for all child nodes
                for (char c = 'a'; c <= 'z'; c++) {
                    TrieNode nextNode = node.children[c - 'a'];
                    if (nextNode != null) {
                        suggestSimilarWordsHelper(nextNode, currentWord + c, suggestions, targetWord);
                    }
                }
            }

        public static void main(String[] args) {
            // create a new instance of trie
            Trie dictionary = new Trie();

            // load vocab into the trie from files
            String filename = "src/main/resources/mobile_plans.csv";

            // try catch just to make sure the file exists or is reachable
            try {
                Scanner csvScan = new Scanner(new File(filename));
                while (csvScan.hasNextLine()) {
                    String currLine = csvScan.nextLine();
                    String[] wordsInString = currLine.split("[^\\w.]+|(?<!\\w)\\.|\\.(?!\\w)", 0);
                    for (String word : wordsInString) {
                        if (!word.trim().isEmpty()) {
                            // Convert the word to lowercase and insert into the Trie
                            dictionary.insertWord(word.toLowerCase());
                        }
                    }
                }
                csvScan.close();
            } catch (FileNotFoundException e) {
                System.out.println("File not found: " + filename);
                return; // Exit if the file cannot be found
            }

            // Scanner for user input
            Scanner userInput = new Scanner(System.in);
            while (true) {
                System.out.println("Enter a word to search (or type 'exit' to quit): ");
                String wordInput = userInput.nextLine();

                if (wordInput.equalsIgnoreCase("exit")) break;

                // Convert to lowercase and check if the word exists in the Trie
                if (dictionary.searchWord(wordInput.toLowerCase())) {
                    System.out.println(wordInput + " was found in the dictionary!");
                } else {
                    // If not found, suggest similar words
                    System.out.println(wordInput + " not found. Closest matches are: " + suggestSimilarWords(dictionary, wordInput.toLowerCase(), 7));
                }
            }
            userInput.close();
        }

    }
}
