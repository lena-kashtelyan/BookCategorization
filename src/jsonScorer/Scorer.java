package jsonScorer;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import csvParser.GenreAndScore;

/**
 * Class to score books obtained from the JSON using hashmaps of
 * keywords obtained from parsing the JSON
 * @author Elena Kashtelyan
 *
 */
public class Scorer {

    /**
     * Comparator for alphabetizing the priority queue of scored books
     */
    Comparator<ScoredBook> byTitle =
            (ScoredBook b1, ScoredBook b2)->b1.getTitle().compareTo(b2.getTitle());

            /* 
             * HashMap holding book titles as keys and book descriptions as values
             */
            private HashMap<String, String> books = new HashMap<String, String>();
            /*
             * The priority queue of books with their genre scores, alphabetized
             */
            private PriorityQueue<ScoredBook> scoredBooks = new PriorityQueue<ScoredBook>(byTitle);

            /**
             * A class to score books using their descriptions and two precomputed hashmaps of keywords
             * @param json - file containing book titles and descriptions
             * @param singletons - hashmap mapping single keywords to lists of genres and respective scores
             * @param twograms - hashmap mapping double keywords to lists of genres and respective scores
             */
            public Scorer(File json, 
                    HashMap<String, List<GenreAndScore>> singletons,
                    HashMap<String, List<GenreAndScore>> twograms) {

                //parse the JSON file
                constructBooksMap(json);
                //retrieve all book titles
                Set<String> bookTitles = books.keySet();
                //score each book
                for (String title : bookTitles) {
                    ScoredBook sB = scoreBook(title, books.get(title), singletons, twograms);
                    scoredBooks.add(sB);
                }
            }

            /**
             * Scores a single book for all genres its description's keywords belong to
             * @param title - the title of the book being scored
             * @param description - the description of the book being scored
             * @param singletons - the hashmap of single keywords with genres and scores
             * @param twograms - the hashmap of double keywords with genres and scores
             * @return a ScoredBook object holding the book's title and genre scores
             */
            private ScoredBook scoreBook(String title, String description,
                    HashMap<String, List<GenreAndScore>> singletons,
                    HashMap<String, List<GenreAndScore>> twograms) {

                //create a new ScoredBook object for the book being scored
                ScoredBook current = new ScoredBook(title);
                //retrieve a set of all keywords
                Set<String> singletonsKS = singletons.keySet();
                Set<String> twogramsKS = twograms.keySet();
                Set<String> keywordSet = new HashSet<String>();
                keywordSet.addAll(singletonsKS);
                keywordSet.addAll(twogramsKS);

                //for each keyword, add points to book's genre evaluation
                for (String keyword : keywordSet) {
                    int beginningChar = 0;
                    int index;
                    while ((index = description.indexOf(keyword, beginningChar)) != -1) {
                        //process single keywords
                        if (singletons.containsKey(keyword)) {
                            for (GenreAndScore gS : singletons.get(keyword)) {
                                current.addPoints(gS.getGenre(), keyword, gS.getScore());
                            }
                        } 
                        //process double keywords
                        else if (twograms.containsKey(keyword)) {
                            for (GenreAndScore gS : twograms.get(keyword)) {
                                 current.addPoints(gS.getGenre(), keyword, gS.getScore());
                            }
                        }
                        //increment the beginning char so that each occurrence of a keyword is only read in once
                        beginningChar = index+keyword.length();
                    }
                }
                //calculate the overall scores
                current.calcScores();
                return current;
            }

            /**
             * Parse the JSON file into 
             * @param json
             */
            private void constructBooksMap(File json) {
            JSONParser parser = new JSONParser();
                Object obj;
                try {
                    obj = parser.parse(new FileReader(json));
                    JSONArray booksJSON = (JSONArray) obj;          
                    for (Object bookJSON : booksJSON) {
                        JSONObject book = (JSONObject) bookJSON;
                        String title = (String) book.get("title");
                        String desc = (String) book.get("description");
                        if (books.containsKey(title)) {
                            System.err.println("ERROR: non-unique book titles "+
                                    "in the JSON file!");
                        } else {
                            books.put(title,  desc);
                        }
                    }
                } catch (IOException | ParseException e) {
                    System.err.println("ERROR: Unable to find or parse the JSON file");
                } 
            }

            /**
             * Prints a single scored book
             */
            public void printBooks() {
                HashMap<Character, Integer> asd = new HashMap<Char, Integer>();
                for (ScoredBook book : scoredBooks) {
                    book.printScoredBook();
                }
                System.out.println();
            }
}
