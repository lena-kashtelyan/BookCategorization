package jsonScorer;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

import csvParser.GenreAndScore;

/**
 * A scored book object that holds the book's title and its genre scores
 * @author Elena Kashtelyan
 *
 */
public class ScoredBook {
    
    /** 
     * Local class to hold an object matching genre to a score
     * @author Elena Kashtelyan
     *
     */
    private class OneGenreScore {
        private int numMatches;
        private HashMap<String, Integer> keywords;
        
        private OneGenreScore() {
            this.numMatches = 0;
            this.keywords = new HashMap<String, Integer>();
        }
        
        private HashMap<String, Integer> getKeywords() {
            return this.keywords;
        }
        
        private int getMatches() {
            return numMatches;
        }
        
        private void incMatches() {
            numMatches++;
        }
        
        /**
         * Calculates the overall score for a single genre
         * @return
         */
        private int calcScore() {
            int pointsNum = 0;
            int points = 0;
            for (int point : keywords.values()) {
                points += point;
                pointsNum++;
            }
            return points/pointsNum * numMatches;
        }
    }
    
    /**
     * Comparator for alphabetizing the priority queue of scored books
     */
    Comparator<GenreAndScore> byScore =
            (GenreAndScore b1, GenreAndScore b2)->Integer.compare(b2.getScore(),b1.getScore()); 
    
    /**
     * String holding the book's title
     */
    private String title;
    /**
     * HashMap matching genres to scores
     */
    private HashMap<String, OneGenreScore> scores;
    /**
     * Final list of GenreAndScore objects holding genres matched
     * to their final scores
     */
    PriorityQueue<GenreAndScore> finalized;
    
    /**
     * Scored book constructor
     * @param title - book title
     */
    public ScoredBook(String title) {
        this.title = title;
        this.scores = new HashMap<String, OneGenreScore>();
        this.finalized = new PriorityQueue<GenreAndScore>(byScore);
    }
    
    public String getTitle() {
        return this.title;
    }
    
    /**
     * Adds poins to the word's genre scores to account for an 
     * appearance of a given keyword in the text
     * @param genre - the genre that the keyword counts towards
     * @param word - the keyword
     * @param points - the number of points the keyword brings to a genre
     */
    public void addPoints(String genre, String word, int points) {
        if (scores.containsKey(genre)) {
            scores.get(genre).incMatches();
            HashMap<String, Integer> kWords = scores.get(genre).getKeywords();
            if (!kWords.containsKey(word)) {
                kWords.put(word, points);
            }
        } else {
            OneGenreScore newScore = new OneGenreScore();
            newScore.incMatches();
            newScore.getKeywords().put(word, points);
            scores.put(genre, newScore);
        }
    }
    
    /**
     * Calculates the final genre scores
     * @return a list of GenreAndScore objects
     * matching genres to their final scores for the given book
     */
    public PriorityQueue<GenreAndScore> calcScores() {
        Set<String> genres = scores.keySet();
        for (String genre : genres) {
            GenreAndScore gS = new GenreAndScore(genre, scores.get(genre).calcScore());
            finalized.add(gS);
        }
        return finalized;
    }
    
    /**
     * Prints a scored book
     */
    public void printScoredBook() {
        System.out.println(this.title);
        int count = 0;
        while(!this.finalized.isEmpty() & count < 3) {
            GenreAndScore gS = finalized.poll();
            System.out.println(gS.getGenre() + ", " + gS.getScore());
            count++;
        }
        System.out.println();
    }
}
