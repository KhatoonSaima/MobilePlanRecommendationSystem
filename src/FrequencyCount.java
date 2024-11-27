// Word frequency calculation using hashtable and heapsort
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class FrequencyCount {

    public static void main(String[] args) throws FileNotFoundException {

        System.out.println("**************************");
        System.out.println("Word Frequency Calculation");
        System.out.println("**************************");

        // Creating object of scanner class to read the file
        Scanner sc = new Scanner(new File("./MergedCSV_File.csv"));
        sc.useDelimiter(",");

        // Initializing hashtable
        Hashtable<String, Integer> wordsCount = new Hashtable<>();

        // Printing each word
        System.out.println("Individual words in the scraped csv file:");

        // Read the csv file and insert words into hashtable
        readFile(sc, wordsCount);

        // Print hash table
        System.out.println("\nHashtable contents (words , occurrence):");
        for (Map.Entry<String, Integer> entry : wordsCount.entrySet()) {
            System.out.println("Word: " + entry.getKey() + ", Occurrence: " + entry.getValue());
        }

        // close the file
        sc.close();

        // Convert Hashtable into list to sort it according to value
        // We can easily sort elements by key without using a list, as the keys are unique, while the values may not be.
        List<Map.Entry<String,Integer>> list = new LinkedList<>(wordsCount.entrySet());

        // Take the user input for number of words to be displayed
        System.out.print("\nHow many most frequent word you want to print: ");
        Scanner sc2 = new Scanner(System.in);
        int N = sc2.nextInt();

        // Using heapsort, sort the list to determine the top N most used words in the file.
        List<Map.Entry<String, Integer>> topN = getNMostUsedWords(list, N);

        // Print the top N words after sorting
        System.out.println("Top " + N + " most frequently used words with its frequency in the file: ");
        for (Map.Entry<String, Integer> entry : topN) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

    // Function to add words with their frequency of occurrence to a hashtable
    public static void insertInHashTable(Hashtable<String, Integer> hashtable , String[] tokens) {
        for (String token : tokens)
        {
            if(hashtable.isEmpty() || !hashtable.containsKey(token))
                hashtable.put(token.trim(), 1);     // first element count should be 1

            int key = (int) hashtable.get(token);
            key++;                              // get the current count value and increment it by one
            hashtable.put(token.trim(), key);
        }

    }

    // Function to read the csv file line by line
    // Extract the word/token after reading each line
    // Input the word into hash table
    public static void readFile(Scanner sc, Hashtable<String, Integer> hashtable)
    {
        // Reading CSV File
        while (sc.hasNext()) {
            String line = sc.nextLine();  // move to next line

            line = line.replaceAll("[\",]", " "); // replace double quotes and comma with single space
            line = line.replaceAll("\\s+", " "); // replace multiple spaces with single space
            line = line.trim();     // remove initial space, otherwise it will consider as token/word
            //System.out.println(line);

            // Split each line based on space and newline
            String[] tokens = line.split("[\\s+]+"); // split with space and newline
            for (String token : tokens)
                System.out.println(token);

            // Insert words into hash table and with its occurrence
            insertInHashTable(hashtable, tokens);
        }
    }

    // Function to get the top N most frequent words using max heap sort
    // returns result, which is Top N most used words
    public static List<Map.Entry<String, Integer>> getNMostUsedWords(List<Map.Entry<String,Integer>> words, int N) {

        // To store the N most used words
        List<Map.Entry<String, Integer>> result = new ArrayList<>(N);

        int n = words.size();

        for (int i = n / 2 - 1; i >= 0; i--) {
            maxHeapify(words, n, i);
        }

        // Get N largest elements
        for (int i = 0; i < N && n > 0; i++) {
            result.add(words.get(0));
            // Move current root to end
            Collections.swap(words, 0, n-1);
            n--;
            maxHeapify(words, n, 0);
        }
        return result;
    }

    // Function to convert the heap into max heap
    public static void maxHeapify(List<Map.Entry<String, Integer>> words, int n, int root) {
        int largest = root;
        int l = 2 * root + 1;
        int r = 2 * root + 2;

        // When left child is larger than root
        if (l < n && words.get(l).getValue() > words.get(largest).getValue()) {
            largest = l;
        }

        // When right child is larger than largest
        if (r < n && words.get(r).getValue() > words.get(largest).getValue()) {
            largest = r;
        }

        // If largest is not equal to root
        if (largest != root) {
            Collections.swap(words, root, largest);

            // Recursively call the maxHeapify
            maxHeapify(words, n, largest);
        }
    }
}
