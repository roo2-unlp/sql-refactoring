import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

public class LikeRefactoring extends Refactoring {
    private String preconditionText = null;
    private CommonTokenStream tokens;

    private SQLiteParser createSQLiteParser(String text) {
        CharStream charStream = CharStreams.fromString(text);
        SQLiteLexer lexer = new SQLiteLexer(charStream);
        this.tokens = new CommonTokenStream(lexer);
        return new SQLiteParser(tokens);
    }

    protected boolean checkPreconditions(String text) {
        SQLiteParser parser = this.createSQLiteParser(text);
        ParseTree newParseTree = parser.parse();

        if (parser.getNumberOfSyntaxErrors() > 0) {
            return false;
        }
        // el % es exclusivo de la clausula LIKE. https://www.sqlite.org/lang_expr.html
        //con visitor que chequea precondiciones
        CheckPreCondVisitor PreCondVisitor = new CheckPreCondVisitor();
        PreCondVisitor.visit(newParseTree);
        return PreCondVisitor.PreConditionsAreMet();
    }

    protected String transform(String text) {
        SQLiteParser parser = this.createSQLiteParser(text);
        ParseTree tree = parser.parse();

        LikeVisitor visitor = new LikeVisitor(this.tokens); 
        String transformedText = visitor.visit(tree);
        return transformedText;
    }

    protected boolean checkPostconditions(String text) {
        SQLiteParser parser = this.createSQLiteParser(text);
        ParseTree newParseTree = parser.parse();
        CheckPostCondVisitor PostCondVisitor = new CheckPostCondVisitor();
        PostCondVisitor.visit(newParseTree);
        return PostCondVisitor.PostConditionsAreMet();
    }
}
