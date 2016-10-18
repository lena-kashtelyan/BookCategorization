package csvParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * CLass to parse the CSV files for keywords
 * @author Elena Kashtelyan
 *
 */
public class Parser {

    private BufferedReader reader;
    private String line = "";
    private String split = ", ";
    private HashMap<String, List<GenreAndScore>> singletons = new HashMap<String, List<GenreAndScore>>();
    private HashMap<String, List<GenreAndScore>> twograms = new HashMap<String, List<GenreAndScore>>();

    /* 
     * Reads input line by line, computing a list of 
     * all words in the input file
     * @argument db - the file to be inspected
     */
    public Parser(File db) {
        try {
            reader = new BufferedReader(new FileReader(db));
            //skip over the first line or complain if file is empty
            if ((line = reader.readLine()) == null) {
                System.err.println("ERROR: keywords file is empty");
            }
            //for all lines of the file, obtain line of the form:
            //"genre, keyword, score"
            while ((line = reader.readLine()) != null) {
                //split on ", "
                String[] split_line = line.split(split);
                //obtain the keyword from the split line
                String keyword = split_line[1];
                //obtain score from the split line
                int score = Integer.parseInt(split_line[2]);
                //obtain genre from the split line
                String genre = split_line[0];
                //create a new GenreAndScore tuple object
                GenreAndScore gS = new GenreAndScore(genre, score);
                //put double keywords into the twograms hashmap
                if (keyword.split(" ").length == 2) {
                    if (twograms.containsKey(split_line[1])) {
                        twograms.get(split_line[1]).add(gS);
                    } else {
                        List<GenreAndScore> genresAndScores = new ArrayList<GenreAndScore>();
                        genresAndScores.add(gS);
                        twograms.put(keyword, genresAndScores);
                    }
                } 
                //put single keywords into the singletons hashmap
                else if (keyword.split(" ").length == 1) {
                    if (singletons.containsKey(split_line[1])) {
                        singletons.get(split_line[1]).add(gS);
                    } else {
                        List<GenreAndScore> genresAndScores = new ArrayList<GenreAndScore>();
                        genresAndScores.add(gS);
                        singletons.put(keyword, genresAndScores);
                    }           
                } else {
                    System.err.println("ERROR: keywords file contains keywords on less than "+
                                                                 "one or more than two words");
                }
            }
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException();
        } catch (IOException e) {
            System.out.println("ERROR: Could not open keywords file in file reader");
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    System.out.println("ERROR: Could not close keywords file in CSV reader");
                }
            }
        }
    }

    /*
     * Gets the resulting list of keywords
     * @returns full list of keywords in the input
     */
    public HashMap<String, List<GenreAndScore>> getSingletons() {
        return this.singletons;
    }

    /*
     * Gets the resulting list of double keywords
     * @returns full list of double keywords words in the input
     */
    public HashMap<String, List<GenreAndScore>> getTwograms() {
        return this.twograms;
    }
    
    /**
     * Prints the a full singleton or twogram hashmap, 
     * useful for testing purposes
     * @param map - the hashmap to be printed
     */
    private void printWordMap(HashMap<String, List<GenreAndScore>> map) {
        Set<String> keys = map.keySet();
        for (String key : keys) {
            System.out.println("Keyword: " + key);
            for (GenreAndScore gS : map.get(key)) {
                System.out.println(gS.getGenre() + ", " + gS.getScore());
            }
        }
    }
}
