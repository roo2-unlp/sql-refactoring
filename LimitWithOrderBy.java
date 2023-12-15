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

        /*CheckPreVisitorSelect visitorSelect = new CheckPreVisitorSelect();
		visitorSelect.visit(newParseTree);
		
		// Validar si es una consutla sql
		if(!visitorSelect.isSelect()){
			return false;
		}*/
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
        SQLiteParser parser = this.createSQLiteParser(text);
        ParseTree tree = parser.parse();
        
        VisitorLimit visitorLimit = new VisitorLimit();
        visitorLimit.visit(tree);
        Boolean isExistLimit = visitorLimit.isValidLimit();
        System.out.println("Texto antes de la transformación: " + text);
        if(isExistLimit){
            // Verificar si la cadena contiene ";"
            if (!text.contains(";")) {
                // Si no contiene, agregar ";" al final
                text += ";";
            }
            return text;
        }
        else{
            text = text.replace(";", "");
            text = text + " LIMIT " + limit + ";";
        }
        
        System.out.println("Texto después de la transformación: " + text);

        return text; 
        
        /* 
        // genero de vuelta el arbol con el texto transformado
        SQLiteParser newParser = this.createSQLiteParser(text);
        ParseTree newTree = newParser.parse();
        
        LimitWithOrderByVisitor visitor = new LimitWithOrderByVisitor();
        String transformedText = visitor.visit(newTree);
        System.out.println("Texto después de la transformación: " + transformedText.toString());

        return transformedText.toString(); 
        */
    }

    // verifica que la consulta despues de la transformacion tenga LIMIT
    protected boolean checkPostconditions(String text) {

        SQLiteParser parser = this.createSQLiteParser(text);
        ParseTree newParseTree = parser.parse();

        if (parser.getNumberOfSyntaxErrors() > 0 ) {
            return false;
        }
    
        VisitorLimit visitorLimit = new VisitorLimit();
        visitorLimit.visit(newParseTree);
        Boolean isExistLimit = visitorLimit.isValidLimit();

        if(!isExistLimit){
            return false;
        }
        
        return true;
    }
}
