
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import sqlitegrammar.*;

public class GroupByRefactoring extends Refactoring {

    private String preconditionText = "ROBERT";

    private SQLiteParser createSQLiteParser(String text) {
        CharStream charStream = CharStreams.fromString(text);
        SQLiteLexer lexer = new SQLiteLexer(charStream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        return new SQLiteParser(tokens);
    }

    @Override
    protected boolean checkPreconditions(String text) {
        SQLiteParser parser = this.createSQLiteParser(text);
        ParseTree newParseTree = parser.parse();

        if (parser.getNumberOfSyntaxErrors() > 0) {
            preconditionText = null;
            return false;
        }
        preconditionText = newParseTree.getText();

        return true;
    }

    @Override
    protected String transform(String text) {
        // Agregar Visitor aca en caso de detectar cual necesitamos tomar de ejemplo el
        // que esta en NullRefactoring
        SQLiteParser parser = this.createSQLiteParser(text);

        ParseTree tree = parser.select_core();
        ParseTree tree2 = parser.column_name();
        System.out.print("tree : ");
        System.out.println(tree);
        System.out.print("tree2 : ");
        System.out.println(tree2);

        System.out.println("");
        System.out.print("text : ");
        System.out.println(text);

        GroupByVisitor visitor = new GroupByVisitor();
        String transformedText = visitor.visit(tree);

        System.out.println("");
        System.out.print("transformedText: ");
        System.out.println(transformedText);

        return transformedText;

    }

    @Override
    protected boolean checkPostconditions(String text) {
        if (preconditionText == "GROUP_BY") {
            return false;
        }
        System.out.println("");
        System.out.print("preconditionstest: ");
        System.out.println(preconditionText);

        return preconditionText.equals(text);
    }

}