import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import sqlitegrammar.*;

public class GroupByRefactoring extends Refactoring{
    private String preconditionText = null;

    private SQLiteParser createSQLiteParser (String text) {
        CharStream charStream = CharStreams.fromString(text);
        SQLiteLexer lexer = new SQLiteLexer(charStream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        return new SQLiteParser(tokens);
    }

    // private boolean checkIncludedDistinct(String text) {
    //     // Check if the text includes a the worid "DISTINCT"
    //     return text.contains("DISTINCT");
        
    // }

    private boolean checkDistinctAfterSelect(String text) {
        // Check if the text includes a the worid "DISTINCT"
        return text.contains("SELECT DISTINCT");
        
    }

    protected boolean checkPreconditions(String text) {
        SQLiteParser parser = this.createSQLiteParser(text);
        ParseTree newParseTree = parser.parse();

        if (parser.getNumberOfSyntaxErrors() == 0 && checkDistinctAfterSelect(text)) {
            preconditionText = newParseTree.getText();
            return true;
        }
        
        preconditionText = null;
        
        return false;
    }
    protected String transform(String text) {
        SQLiteParser parser = this.createSQLiteParser(text);
        ParseTree tree = parser.parse();

        NullVisitor visitor = new NullVisitor();
        String transformedText = visitor.visit(tree);
        
        return transformedText;
    }
    protected boolean checkPostconditions(String text) {
        if (preconditionText == null) {
            return false;
        }

        return preconditionText.equals(text);
    }
}
