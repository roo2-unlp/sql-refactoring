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

        //preconditionText = newParseTree.getText();
        preconditionText = text;

        // el % es exclusivo de la clausula LIKE. https://www.sqlite.org/lang_expr.html
        if (!preconditionText.contains("%")) {
            return false;
        }

        return true;

        //con visitor que chequea precond
        // CheckPreCondVisitor PreCondVisitor = new CheckPreCondVisitor();
        // PreCondVisitor.visit(newParseTree);
        // return PreCondVisitor.PreConditionsAreMet();
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
        //aca podria ir un visitor chequeando la post condicion (que la expr del like tenga el % a lo ultimo)
        //creo que va un visitor porque al solo poder modificar la consulta no tenemos valores de respuesta a las consultas de los test que coincidan
        //con visitor que chequea postcond
        // CheckPostCondVisitor PostCondVisitor = new CheckPostCondVisitor();
        // PostCondVisitor.visit(newParseTree);
        // return PostCondVisitor.PostConditionsAreMet();

        return preconditionText.equals(text);
    }
}
