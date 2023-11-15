import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import sqlitegrammar.*;

public class GroupByRefactoring extends Refactoring{
    private String preconditionText = null;

    private SQLiteParser createSQLiteParser (String text) {
        CharStream charStream = CharStreams.fromString(text);
        SQLiteLexer lexer = new SQLiteLexer(charStream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        return new SQLiteParser(tokens);
    }

    // private boolean checkIncludedSelect(ParseTree tree) {
    //     // Check if the text includes a the worid "DISTINCT"
    //     SelectDistinctVisitor visitor = new SelectDistinctVisitor();
    //     return visitor.visit(tree);
        
    // }

    private boolean checkDistinctAfterSelect(String text) {
        // Check if the text includes a the worid "DISTINCT"
        return text.contains("SELECT DISTINCT");
        
    }

    protected boolean checkPreconditions(String text) {
        SQLiteParser parser = this.createSQLiteParser(text);
        ParseTree newParseTree = parser.parse();
        SelectDistinctVisitor visitor = new SelectDistinctVisitor();

        /*if (parser.getNumberOfSyntaxErrors() == 0 && checkDistinctAfterSelect(text)) {
            preconditionText = newParseTree.getText();
            return true;
        }*/
        
        String transformedText = visitor.visit(newParseTree);
        System.out.println("Print del Refactoring: transformedText");
        System.out.println(transformedText);
        if (transformedText.contains("SELECT DISTINCT")) {
            //preconditionText = newParseTree.getText();
            System.out.println("entre a la condicion");
            return true;
        }

        preconditionText = null;
        
        return false;
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
