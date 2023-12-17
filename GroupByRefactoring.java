import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;


public class GroupByRefactoring extends Refactoring{
    
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
            return false;
        }

        if (!preConditionsVisitor.getContainsDistinct()) {
            return false;
        }

        if (preConditionsVisitor.getContainsAggregateFunction()) {
            return false;
        }
        
        if (preConditionsVisitor.getContainsGroupBy()) {
            return false;
        }

        if (preConditionsVisitor.getContainsStar()) {
            return false;
        }

        return true;
    }
    protected String transform(String text) {
        SQLiteParser parser = this.createSQLiteParser(text);
        ParseTree tree = parser.parse();

        TransformerVisitor transformerVisitor = new TransformerVisitor();
        transformerVisitor.visit(tree);
        
        TerminalNodesToTextVisitor textVisitor = new TerminalNodesToTextVisitor();
        String transformedText = arreglarString(textVisitor.visit(tree));

        return transformedText;
    }

    private String arreglarString(String text) {
        if (text.contains(";")) {
            return text.toString().replaceAll(" ; <EOF>", ";").replaceAll(" ,", ",").trim();
        }
        else {
            return text.toString().replaceAll(" <EOF>", "").replaceAll(" ,", ",").trim();
        }
	}

    protected boolean checkPostconditions(String text) {
        SQLiteParser parser = this.createSQLiteParser(text);
        ParseTree newParseTree = parser.parse();
        
        PrePostConditionsVisitor postConditionsVisitor = new PrePostConditionsVisitor();
        
        postConditionsVisitor.visit(newParseTree);
        if (parser.getNumberOfSyntaxErrors() > 0) {
            return false;
        }

        if (postConditionsVisitor.getContainsDistinct()) {
            return false;
        }
        
        if (!postConditionsVisitor.getContainsGroupBy()) {
            return false;
        }

        return true;
    }
}
