import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;


public class GroupByRefactoring extends Refactoring{
    private String preconditionText;

    public GroupByRefactoring() {
        super();
        this.preconditionText = null;
    }
    
    private SQLiteParser createSQLiteParser (String text) {
        CharStream charStream = CharStreams.fromString(text);
        SQLiteLexer lexer = new SQLiteLexer(charStream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        SQLiteParser parser = new SQLiteParser(tokens);
        return parser;
    }

    protected boolean checkPreconditions(String text) {
        SQLiteParser parser = this.createSQLiteParser(text);
        ParseTree newParseTree = parser.parse();

        
        PrePostConditionsVisitor preConditionsVisitor = new PrePostConditionsVisitor();
        
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
        
        if (preConditionsVisitor.getContainsGroupBy()) {
            preconditionText = newParseTree.getText();
            return false;
        }

        // if (!preconditionText.equals(text)) {
        //     System.out.println("Precondition text: " + preconditionText);
        //     System.out.println("Text: " + text);
        //     return false;
        // }
        return true;
    }
    protected String transform(String text) {
        SQLiteParser parser = this.createSQLiteParser(text);
        ParseTree tree = parser.parse();

        TransformerVisitor visitor = new TransformerVisitor();
        visitor.visit(tree);
        
        TextVisitor visitorText = new TextVisitor();
        String transformedText = arreglarString(visitorText.visit(tree));
        return transformedText;
    }
    private String arreglarString(String text) {
        return text.toString().trim() + ";";
	}
    protected boolean checkPostconditions(String text) {
        SQLiteParser parser = this.createSQLiteParser(text);
        ParseTree newParseTree = parser.parse();

        
        PrePostConditionsVisitor postConditionsVisitor = new PrePostConditionsVisitor();
        
        postConditionsVisitor.visit(newParseTree);
        if (parser.getNumberOfSyntaxErrors() > 0) {
            preconditionText = newParseTree.getText();
            return false;
        }

        if (postConditionsVisitor.getContainsDistinct()) {
            preconditionText = newParseTree.getText();
            return false;
        }
        if (postConditionsVisitor.getContainsAggregateFunction()) {
            preconditionText = newParseTree.getText();
            return false;
        }
        
        if (!postConditionsVisitor.getContainsGroupBy()) {
            preconditionText = newParseTree.getText();
            return false;
        }
        return true;
    }
}
