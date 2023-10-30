import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import sqlitegrammar.*;

public class LikeRefactoring extends Refactoring {
    private String preconditionText = null;

    public String refactor(String text) throws RefactoringException {
        if (!this.checkPreconditions(text)) {
            throw new RefactoringException("Preconditions not met.");
        }
        String refactoredText = this.transform(text);
        if (!this.checkPostconditions(refactoredText)) {
            throw new RefactoringException("Postconditions not met.");
        }
        return refactoredText;
    }

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

        if (!preconditionText.contains("%")) {
            return false;
        }

        return true;
    }

    protected String transform(String text) {
        SQLiteParser parser = this.createSQLiteParser(text);
        ParseTree tree = parser.parse();

        LikeVisitor visitor = new LikeVisitor(); // creo el visitor
        String transformedText = visitor.visitParse(visitor.visitLiteral_value(tree)); // supongo que va asi.. hace la
                                                                                       // transformacion del contenido
                                                                                       // del visitor de esa regla

        return transformedText;
    }

    protected boolean checkPostconditions(String text) {
        if (preconditionText == null) {
            return false;
        }

        return preconditionText.equals(text);
    }
}
