import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;
import sqlitegrammar.*;

class CountRefactoring extends Refactoring {
    private String preconditionQueryText;

    private SQLiteParser createSQLiteParser(String text) {
        CharStream charStream = CharStreams.fromString(text);
        SQLiteLexer lexer = new SQLiteLexer(charStream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        return new SQLiteParser(tokens);
    }

    protected boolean checkPreconditions(String query) {
        SQLiteParser parser = this.createSQLiteParser(query);
        ParseTree newParseTree = parser.parse();
        if (parser.getNumberOfSyntaxErrors() > 0){
            return false;
        }
        this.preconditionQueryText = query;
        CountFinderVisitor visitor = new CountFinderVisitor();
        visitor.visit(newParseTree);
        return  visitor.existCountFunctionWithStar();
    }

    protected String transform(String text) {
        SQLiteParser parser = this.createSQLiteParser(text);
        ParseTree tree = parser.parse();

        CountVisitor visitor = new CountVisitor();
        String transformedText = visitor.visit(tree);//Falta implementar
        return transformedText.replace("<EOF>", "");
    }

    protected boolean checkPostconditions(String text) {
        return !text.equalsIgnoreCase("preconditionQueryText");
        /* Basta con analizar que sean distintas una vez asumidas las pre condiciones?
        * Envio cómo string la variable hasta que se desarrolle la funcionalidad
        */
    }
}