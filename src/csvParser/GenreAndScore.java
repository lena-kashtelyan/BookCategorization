package csvParser;

/**
 * Container class holding matchings of genres and scores
 * @author Elena Kashtelyan
 *
 */
public class GenreAndScore {
    
    private String genre;
    private int score;
    
    public GenreAndScore(String genre, int score) {
        this.genre = genre;
        this.score = score;
    }
    
    public String getGenre() {
        return this.genre;
    }
    
    public int getScore() {
        return this.score;
    }
}
