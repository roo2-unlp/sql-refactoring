
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;


import sqlitegrammar.*;

public class GroupByRefactoring extends Refactoring {

    private String preconditionText = "GROUP BY";
    private String stmtParameter;

    
   

    public void setStmtParameter(String stmtParemeter){
        this.stmtParameter=stmtParemeter;
    }

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
   
        //Preguntar en caso que sea un sql sintacticamente correcto pero sin group by 
        //Preguntar en caso que sea un sql sintacticamente incorrecto sin group by deberiamos tomarlo como algo que se puede transformar? ya que el getNumberOfSystaxErrors ej: Select name from persona p name; donde faltaria agregar el group by al final de p ?
        
        if (parser.getNumberOfSyntaxErrors()>0) {
            return false;            
        }
        return true;
    }

    @Override
    protected String transform(String text) {
        // Agregar Visitor aca en caso de detectar cual necesitamos tomar de ejemplo el
        // que esta en NullRefactoring
        
        SQLiteParser parser = this.createSQLiteParser(text);        
        ParseTree tree = parser.parse(); 
        GroupByVisitor visitor = new GroupByVisitor();

        String transformedText= visitor.visit(tree);  
        StringBuilder finalTransformed = new StringBuilder();
        //TODO agregar el stmtParemeter donde necesitemos cambiarlo, por ejemplo dentro del select y en el group by 
        finalTransformed.append(transformedText)
        .delete(finalTransformed.indexOf(";<EOF>", 0), finalTransformed.capacity());
        
        finalTransformed.append(preconditionText+" "+this.stmtParameter+";");
        
        return finalTransformed.toString();
        
    }

    @Override
    protected boolean checkPostconditions(String text) {
        
        SQLiteParser parser = this.createSQLiteParser(text);        
        System.out.println("text en post conditions");
        System.out.println(text);
        ParseTree tree = parser.parse();         
        if (parser.getNumberOfSyntaxErrors() > 0 && parser.select_core().groupByExpr == null) {
            return false;            
        }          

        return preconditionText.equals(text);
    }   
}