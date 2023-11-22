import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import sqlitegrammar.*;

public class LimitWithOrderBy extends Refactoring{
    private String preconditionText = null;

    private SQLiteParser createSQLiteParser (String text) {
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
        return true;
    }

    /*
    protected String transform(String text) {
        SQLiteParser parser = this.createSQLiteParser(text);
        ParseTree tree = parser.parse();

        LimitWithOrderByVisitor visitor = new LimitWithOrderByVisitor();
        String transformedText = visitor.visit(tree);
        
        return transformedText;
    }
    */

    protected String transform(String text) {
        SQLiteParser parser = this.createSQLiteParser(text);
        ParseTree tree = parser.parse();

        // instancio el visitor
        LimitWithOrderByVisitor visitor = new LimitWithOrderByVisitor();
        visitor.visit(tree);

        if (!visitor.hasLimit()) { // Cambié de limitChecker a visitor
            // La consulta original no tiene LIMIT, así que debemos agregar uno.
            SQLiteParser.Limit_stmtContext limitContext = parser.limit_stmt();
            if (limitContext == null) {
                // Si no hay LIMIT en la consulta original, creamos uno con un valor predeterminado.
                text = text + " LIMIT 10";
            }
        }

        // Obtiene el texto transformado.
        String transformedText = visitor.getText();
        return transformedText;
    }

    protected boolean checkPostconditions(String text) {
        if (preconditionText == null) {
            return false;
        }

        return preconditionText.equals(text);
    }
}
