
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;


import sqlitegrammar.*;

public class GroupByRefactoring extends Refactoring {

    private String preconditionText = "GROUP BY";

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
        finalTransformed.append(transformedText)
        .delete(finalTransformed.indexOf(";<EOF>", 0), finalTransformed.capacity());
        
        finalTransformed.append(preconditionText+";");
        
        System.out.println("Final transformed");
        System.out.println(finalTransformed);
        return finalTransformed.toString();
        
    }

    @Override
    protected boolean checkPostconditions(String text) {
        
        SQLiteParser parser = this.createSQLiteParser(text);        
        ParseTree tree = parser.parse();         
        if (parser.getNumberOfSyntaxErrors() > 0 && parser.select_core().groupByExpr == null) {
            return false;            
        }          

        return preconditionText.equals(text);
    }   
}