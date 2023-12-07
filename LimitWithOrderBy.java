import java.util.List;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import sqlitegrammar.*;

public class LimitWithOrderBy extends Refactoring{
    private String preconditionText = null;
    private int limit=10;

    public void setLimit(int limnit){
        this.limit = limit;
    }
    // queda igual
    private SQLiteParser createSQLiteParser (String text) {
        CharStream charStream = CharStreams.fromString(text);
        SQLiteLexer lexer = new SQLiteLexer(charStream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        return new SQLiteParser(tokens);
    }

    protected boolean checkPreconditions(String text) {
        SQLiteParser parser = this.createSQLiteParser(text);
        ParseTree newParseTree = parser.parse();

        if (parser.getNumberOfSyntaxErrors() > 0 || !isExistOrderBy ) {
            return false;
        }

        CheckPreVistor checkPreVistor = new CheckPreVistor();
        checkPreVistor.visit(newParseTree);
        Boolean isExistOrderBy = checkPreVistor.isValidPre();

        return true;
        //preconditionText = newParseTree.getText();
    }
 
    @Override
    protected String transform(String text) {
        System.out.println("Texto antes de la transformación: " + text);
        SQLiteParser parser = this.createSQLiteParser(text);
        ParseTree tree = parser.parse();
       
        LimitWithOrderByVisitor visitor = new LimitWithOrderByVisitor();
        String transformedText = visitor.visit(tree);
        System.out.println("Texto después de la transformación: " + transformedText);
        System.out.println("transform se está ejecutando");

        return transformedText; 
    }

    // verifica que la consulta despues de la transformacion tenga LIMIT
    protected boolean checkPostconditions(String text) {

        SQLiteParser parser = this.createSQLiteParser(text);
        ParseTree newParseTree = parser.parse();

        if (parser.getNumberOfSyntaxErrors() > 0 || !isExistLimit ) {
            return false;
        }
        
        CheckPostVistor checkPostVistor = new CheckPostVistor();
        checkPostVistor.visit(newParseTree);
        Boolean isExistLimit = checkPostVistor.isValidPost();
        
        return true;
    }
}
