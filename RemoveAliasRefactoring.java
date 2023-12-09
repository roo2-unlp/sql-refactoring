
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

//import grammarSQLite.*;
// import sqlitegrammar.SQLiteParser;
// import SQLiteParserBaseVisitor.RemoveAliasVisitor;


public class RemoveAliasRefactoring extends Refactoring{
    private String preconditionText = null;   
    private String alias="";
    private String aliasReference="";
    

    private SQLiteParser createSQLiteParser (String text) {
        CharStream charStream = CharStreams.fromString(text);
        SQLiteLexer lexer = new SQLiteLexer(charStream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        return new SQLiteParser(tokens);
    }

    protected boolean checkPreconditions(String text) {//el text que representa la query
        //this.setAlias("tnm");
        SQLiteParser  parser = this.createSQLiteParser(text);
        ParseTree newParseTree = parser.parse();
        AliasCheckerVisitor visitorCheck= new AliasCheckerVisitor();
        if (parser.getNumberOfSyntaxErrors() > 0) { //Revisa que la sintaxis sea exitoso
            preconditionText = null;
            return false;
        }
        visitorCheck.setAlias(getAlias());
        visitorCheck.visit(newParseTree);
        
        if(visitorCheck.getAliasEncontrado()){  // verifica que el alias existe y retorna true o false
            // aca me tengo que quedar con el nombre de la tabla o columna si el alias existe 
            preconditionText = newParseTree.getText();
           this.setAliasReference(visitorCheck.getAliasReference());
            return true;
        }
        else
            {return false;}
    }
    protected String transform(String text) {  
        SQLiteParser parser = this.createSQLiteParser(text);
        ParseTree tree = parser.parse();      
        RemoveAliasVisitor visitor = new RemoveAliasVisitor();
        visitor.setAlias(alias);
        visitor.setAliasReference(aliasReference);   
        visitor.visit(tree);     
        String transformedText = visitor.getQuerySeparete();
        System.out.println(); 
        System.out.println("TEXTO ORIGINAL");
        System.out.println(text);
        System.out.println();
        System.out.println(); 
        System.out.println("TEXTO MODIFICADO");
        System.out.println(transformedText);
        System.out.println();
        return transformedText;
        
    }
    protected boolean checkPostconditions(String text) {
        SQLiteParser parser = this.createSQLiteParser(text);
        ParseTree tree = parser.parse();
        AliasCheckerVisitor visitor= new AliasCheckerVisitor();
        visitor.visit(tree);
        return !visitor.getAliasEncontrado();
   
         
        
         // deberia cheakear si el alias fue eliminado
    }
    
    
    public void setAlias(String alias){
        this.alias=alias;
    }
    public String getAlias(){
        return this.alias;
    }
    public void setAliasReference(String aliasReference){
        this.aliasReference=aliasReference;
    }
    public String getAliasReference(){
        return this.aliasReference;
    }



    public void printParseTree(ParseTree tree, int indent) {
        if (tree != null) {
            for (int i = 0; i < indent; i++) {
                System.out.print("  "); // Espacios para la indentación
            }
            System.out.println(tree.getText()); // Imprimir el texto del nodo
            
            // Recorrer los hijos del árbol
            // for (int i = 0; i < tree.getChildCount(); i++) {
            //     printParseTree(tree.getChild(i), indent + 1); // Llamar recursivamente a cada hijo
            // }
        }
    }
    


}

