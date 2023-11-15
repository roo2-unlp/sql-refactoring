import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
//import sqlitegrammar..*;

public class LikeRefactoring extends Refactoring {
    private String preconditionText = null;

    private SQLiteParser createSQLiteParser(String text) {
        CharStream charStream = CharStreams.fromString(text);
        SQLiteLexer lexer = new SQLiteLexer(charStream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        return new SQLiteParser(tokens);
    }

    protected boolean checkPreconditions(String text) {
        SQLiteParser parser = this.createSQLiteParser(text);
        ParseTree newParseTree = parser.parse();

        if (parser.getNumberOfSyntaxErrors() > 0) {
            preconditionText = null;
            return false;
        }

        preconditionText = newParseTree.getText();
        // el % es exclusivo de la clausula LIKE. https://www.sqlite.org/lang_expr.html
        if (!preconditionText.contains("%")) {
            return false;
        }

        return true;
    }

    protected String transform(String text) {
        SQLiteParser parser = this.createSQLiteParser(text);
        ParseTree tree = parser.parse();

        LikeVisitor visitor = new LikeVisitor(); 
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
