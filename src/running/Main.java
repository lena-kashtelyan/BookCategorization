package running;

import java.io.File;

import csvParser.Parser;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;
import jsonScorer.Scorer;

/**
 * Main class, to be run with paths to CSV and JSON files as arguments
 * @author Elena Kashtelyan
 *
 */
public class Main {

    public static void main(String[] args) {
        new Main(args).run();
    }

    private String[] args;

    private Main(String[] args) {
        this.args = args;
    }
    
    private File books;
    private File keywords;
    private Parser parser;
    private Scorer scorer;

    /*
     * Runs the program
     */
    private void run() {

        OptionParser parser = new OptionParser();
        //obtain the filenames from the command line inputs
        OptionSpec<File> files = parser.nonOptions().ofType( File.class );
        OptionSet options = parser.parse(args);
        
        //make sure inputs has two files
        if (args.length != 2) {
            System.err.println("Usage is: <JSON books file> <CSV keywords file>");
            System.exit(1);
        } 
        
        books = options.valuesOf(files).get(0);
        keywords = options.valuesOf(files).get(1);
        if (books == null) {
            System.err.println("ERROR: Could not find books file");
            System.exit(1);
        } else if (keywords == null) {
            System.err.println("ERROR: Could not find keywords file");
            System.exit(1);
        } else {
            //parse the CSV file into keyword hashmaps
            Parser keywordsParser = new Parser(keywords);
            //score books using those hashmaps
            scorer = new Scorer(books, keywordsParser.getSingletons(),
                    keywordsParser.getTwograms());
            //print output
            scorer.printBooks();
        }
    }
}
