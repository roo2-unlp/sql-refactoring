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

    protected Boolean containsAgragetionFunc(String result) {
        return (result.contains("COUNT(") || result.contains("SUM(") || result.contains("AVG(") || result.contains("MIN(") || result.contains("MAX(")); 
    }

    protected boolean checkPreconditions(String text) {
        SQLiteParser parser = this.createSQLiteParser(text);
        ParseTree newParseTree = parser.parse();
        SelectDistinctVisitor visitor = new SelectDistinctVisitor();

        String result = visitor.visit(newParseTree).toUpperCase();

        if (parser.getNumberOfSyntaxErrors() > 0) {
            preconditionText = newParseTree.getText();
            return false;
        }
        if (!result.contains("SELECTDISTINCT")) {
            preconditionText = newParseTree.getText();
            return false;
        }
        if (containsAgragetionFunc(result)) {
            preconditionText = newParseTree.getText();
            return false;
        }
        if (result.contains("GROUPBY")) {
            preconditionText = newParseTree.getText();
            return false;
        }

        preconditionText = null;
        
        return true;
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
