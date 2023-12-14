import java.util.List;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import sqlitegrammar.*;

public class LimitWithOrderBy extends Refactoring{
    private String preconditionText = null;
    private int limit=10;

    public void setLimit(int limit){
        this.limit = limit;
    }

    private SQLiteParser createSQLiteParser (String text) {
        CharStream charStream = CharStreams.fromString(text);
        SQLiteLexer lexer = new SQLiteLexer(charStream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        return new SQLiteParser(tokens);
    }

    protected boolean checkPreconditions(String text) {
        SQLiteParser parser = this.createSQLiteParser(text);
        ParseTree newParseTree = parser.parse();

        // Validar que no tenga errores de sintaxis
        if (parser.getNumberOfSyntaxErrors() > 0) {
            return false;
        }

        CheckPreVisitorSelect visitorSselect = new CheckPreVisitorSelect();
		visitorSelect.visit(newParseTree);
		
		// Validar si es una consutla sql
		if(!visitorSelect.isSelect()){
			return false;
		}
        CheckPreVisitor checkPreVisitor = new CheckPreVisitor();
        checkPreVisitor.visit(newParseTree);
        Boolean isExistOrderBy = checkPreVisitor.isValidPre();

        // Validar que tenga el order by
        if(!isExistOrderBy){
            return false;
        }

        return true;
    }
 
    @Override
    protected String transform(String text) {
        System.out.println("Texto antes de la transformación: " + text);
        SQLiteParser parser = this.createSQLiteParser(text);
        ParseTree tree = parser.parse();
       
        LimitWithOrderByVisitor visitor = new LimitWithOrderByVisitor();
        visitor.setLimit(limit);
        String transformedText = visitor.visit(tree);
        System.out.println("Texto después de la transformación: " + transformedText);

        return transformedText; 
    }

    // verifica que la consulta despues de la transformacion tenga LIMIT
    protected boolean checkPostconditions(String text) {

        SQLiteParser parser = this.createSQLiteParser(text);
        ParseTree newParseTree = parser.parse();

        if (parser.getNumberOfSyntaxErrors() > 0 ) {
            return false;
        }
    
        CheckPostVisitor checkPostVisitor = new CheckPostVisitor();
        checkPostVisitor.visit(newParseTree);
        Boolean isExistLimit = checkPostVisitor.isValidPost();

        if(!isExistLimit){
            return false;
        }
        
        return true;
    }
}
