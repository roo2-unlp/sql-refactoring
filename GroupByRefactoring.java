import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;


public class GroupByRefactoring extends Refactoring{
    private String preconditionText = null;

    private SQLiteParser createSQLiteParser (String text) {
        CharStream charStream = CharStreams.fromString(text);
        SQLiteLexer lexer = new SQLiteLexer(charStream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        SQLiteParser parser = new SQLiteParser(tokens);
        parser.setErrorHandler(new BailErrorStrategy());
        return parser;
    }

    protected boolean checkPreconditions(String text) {
        SQLiteParser parser = this.createSQLiteParser(text);
        ParseTree newParseTree = parser.parse();

        
        PreConditionsVisitor preConditionsVisitor = new PreConditionsVisitor();
        
        preConditionsVisitor.visit(newParseTree);
        if (parser.getNumberOfSyntaxErrors() > 0) {
            preconditionText = newParseTree.getText();
            return false;
        }

        if (!preConditionsVisitor.getContainsDistinct()) {
            preconditionText = newParseTree.getText();
            return false;
        }
        if (preConditionsVisitor.getContainsAggregateFunction()) {
            preconditionText = newParseTree.getText();
            return false;
        }
        
        if (preConditionsVisitor.getContainsGroup()) {
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
