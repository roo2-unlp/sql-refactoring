import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import sqlitegrammar.*;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

public class ReplaceOrVisitor extends SQLiteParserBaseVisitor<Boolean> {

    private boolean visitOr;

   @Override
   public Boolean visitExpr(SQLiteParser.ExprContext ctx) {
       System.out.println("------------PreConditionsVisitor--------------");
       System.out.println("ctx  " + ctx.getText());
       System.out.println("ctx.OR_()  " + ctx.OR_());
        if (ctx.OR_() != null) {
            this.visitOr = true;
            return true; // Devuelve true si encuentra OR
        } else {
            this.visitOr = false;
            return false; // Devuelve false si no encuentra OR
        }
    }
   

    public boolean getVisitOr(){
        return this.visitOr;
    }




    
}
