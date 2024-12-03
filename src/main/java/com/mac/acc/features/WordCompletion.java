package com.mac.acc.features;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static java.lang.System.out;

public class WordCompletion{
    //node class
    class NodeT{
        NodeT cld[] = new NodeT[26];
        boolean eow = false;// end of the word
        public NodeT() {
            for(int i=0; i<26; i++){
                cld[i] = null;
            }
        }
    }

    public NodeT toproot = new NodeT();

    //method of insert a word into the trie
    public void insert(String word) {
        NodeT current = toproot;
        for(int level=0; level<word.length(); level++){
            int idx = word.charAt(level) - 'a';
            if(current.cld[idx] == null){
                current.cld[idx] = new NodeT();
            }
            current = current.cld[idx];
        }
        current.eow = true;

    }


    // print the whole trie
    public void printTrie() {
        printHelper(toproot, "");
    }

    public void printHelper(NodeT node, String prex) {
        if (node.eow) {
            out.println(prex); // when a word, print it out
        }
        for (int i = 0; i < 26; i++) {
            if (node.cld[i] != null) {
                char childChar = (char) (i + 'a');
                printHelper(node.cld[i], prex + childChar); // recursively print out all nodes
            }
        }
    }
    // find the word start with the given prex
    public List<String> prefixfinder(String prex) {
        List<String> completions = new ArrayList<>();

        NodeT curr = toproot;
        for (int i = 0; i < prex.length(); i++) {
            char c = prex.charAt(i);
            // Check if the character is a valid lowercase letter
            if (c < 'a' || c > 'z') {
                System.out.println("Invalid input: prefix contains non-alphabetic character \"" + c + "\"");
                return completions; // Return an empty list if invalid character found
            }
            int index = c - 'a';
            if (curr.cld[index] == null) {
                return completions; // Return empty if prefix does not exist
            }
            curr = curr.cld[index];
        }

        // Collect all words starting from this node
        collectWords(curr, prex, completions);
        return completions;
    }


    // Helper method to collect words recursively
    private void collectWords(NodeT node, String prex, List<String> completions) {
        if (node.eow) {
            completions.add(prex);
        }
        for (int i = 0; i < 26; i++) {
            if (node.cld[i] != null) {
                char childChar = (char) (i + 'a');
                collectWords(node.cld[i], prex + childChar, completions);
            }
        }
    }

    public static void main(String[] args) {
        WordCompletion trie = new WordCompletion();

        //vocabulary
        String csvFile = "src/main/resources/mobile_plans.csv";
        String line;
        String delimiter = ",";

        // establish trie
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            while ((line = br.readLine()) != null) {
                // row
                String[] cells = line.split(delimiter);

                for (String cell : cells) {
                    // eliminate all the non-letter character,only save letter and change it into lower-case letter
                    String cleanedCell = cell.replaceAll("[^a-zA-Z\\s]", "").toLowerCase();

                    // split string with single blankspace
                    String[] words = cleanedCell.trim().split("\\s+");
                    // implement the trie
                    for (String word : words) {
                        trie.insert(word); // insert the vocabulary
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // print the trie
        out.println("Words in Trie:");
        trie.printTrie();

        // input
        out.println("Please enter a prefix : ");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        //output
        out.println("Words start with : “"+input+"”");
        trie.prefixfinder(input);

        // Find and display completions
        List<String> completions = trie.prefixfinder(input);
        if (completions.isEmpty()) {
            System.out.println("No words found with prefix \"" + input + "\".");
        } else {
            System.out.println("Words starting with \"" + input + "\": " + completions);
        }
    }

}


