import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import sqlitegrammar.*;

class CountRefactoring extends Refactoring {

    private SQLiteParser createSQLiteParser(String text) {
        CharStream charStream = CharStreams.fromString(text);
        SQLiteLexer lexer = new SQLiteLexer(charStream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        return new SQLiteParser(tokens);
    }

    protected boolean checkPreconditions(String text) {
        SQLiteParser parser = this.createSQLiteParser(text);
        ParseTree newParseTree = parser.parse();

        return (parser.getNumberOfSyntaxErrors() > 0 || !text.toLowerCase().contains("count(*)"));

    }
    protected String transform(String text) {
        SQLiteParser parser = this.createSQLiteParser(text);
        ParseTree tree = parser.parse();

        CountVisitor visitor = new CountVisitor();
        String transformedText = visitor.visit(tree);
        
        return transformedText;
    }
    protected boolean checkPostconditions(String text) {
        return !text.toLowerCase().contains("count(*)");// Deberia agregar antes el select?

    }
}