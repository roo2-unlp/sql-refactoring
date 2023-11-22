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

        //Revisa que la sintaxis sea exitoso
        if (parser.getNumberOfSyntaxErrors() > 0) { 
            preconditionText = null;
            return false;
        }
        // Verifica si la consulta contiene la cláusula ORDER BY
        SQLiteParser.Ordering_termContext orderByContext = parser.ordering_term();
        if (orderByContext == null) {
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

    // verifica que la consulta despues de la transformacion tenga LIMIT
    protected boolean checkPostconditions(String text) {
        SQLiteParser parser = this.createSQLiteParser(text);
        ParseTree tree = parser.parse();
        LimitWithOrderByVisitor visitor = new LimitWithOrderByVisitor();
        visitor.visit(tree);
        return !visitor.visitLimit_stmt(text);
   
        //if (preconditionText == null) {
        //    return false;
        // }
        //return preconditionText.equals(text);
    }
}
