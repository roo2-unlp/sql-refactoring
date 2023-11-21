import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;
import sqlitegrammar.*;

class CountRefactoring extends Refactoring {
    private CommonTokenStream tokens;
    private String columnName;

    public CountRefactoring(String columnName) {
        this.columnName = columnName;
    }

    protected boolean checkPreconditions(String query) {
        SQLiteParser parser = this.createSQLiteParser(query);
        ParseTree newParseTree = parser.parse();
        if (parser.getNumberOfSyntaxErrors() > 0){
            return false;
        }
        CountFinderVisitor visitor = new CountFinderVisitor();
        visitor.visit(newParseTree);
        return  visitor.existCountFunctionWithStar();
    }

    protected String transform(String text) {
        SQLiteParser parser = this.createSQLiteParser(text);
        ParseTree tree = parser.parse();
        CountVisitor visitor = new CountVisitor(tokens, this.columnName);
        String transformedText = visitor.visit(tree);
        return transformedText.replace("<EOF>", "");
    }

    protected boolean checkPostconditions(String query) {
        SQLiteParser parser = this.createSQLiteParser(query);
        ParseTree newParseTree = parser.parse();
        if (parser.getNumberOfSyntaxErrors() > 0){
            return false;
        }
        CountFinderVisitor visitor = new CountFinderVisitor();
        visitor.visit(newParseTree);
        return ! visitor.existCountFunctionWithStar();
    }

    private SQLiteParser createSQLiteParser(String text) {
        CharStream charStream = CharStreams.fromString(text);
        SQLiteLexer lexer = new SQLiteLexer(charStream);
        this.tokens = new CommonTokenStream(lexer);
        return new SQLiteParser(tokens);
    }
}