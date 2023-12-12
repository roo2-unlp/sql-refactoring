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

        preconditionText = null;
        return true;
    }
    protected String transform(String text) {
        SQLiteParser parser = this.createSQLiteParser(text);
        ParseTree tree = parser.parse();

        TransformerVisitor visitor = new TransformerVisitor();
        visitor.visit(tree);
        String transformedText = "prueba";

        
        TextVisitor visitorText = new TextVisitor();
        String res = visitorText.visit(tree);
        //System.out.println("TextVisitor PRINT: "+res);
        transformedText = visitorText.getTransformedText();
        //System.out.println("RefactoringTransform PRINT: "+transformedText);
        transformedText = arreglarString(visitorText.visit(tree));
        return arreglarString(transformedText);
    }
    private String arreglarString(String conEspacios) {
        return conEspacios.toString().trim() + ";";
		//return conEspacios.toString().replaceAll("\\s*\\.\\s*", ".").replaceAll(" ; <EOF>", ";");
	}
    protected boolean checkPostconditions(String text) {
        if (preconditionText == null) {
            return false;
        }

        return preconditionText.equals(text);
    }
}
