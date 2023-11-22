import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import sqlitegrammar.*;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

public class ReplaceOrVisitor extends SQLiteParserBaseVisitor<Boolean> {

    private boolean visitOr;

   @Override
   public Boolean visitExpr(SQLiteParser.ExprContext ctx) {
        // Verificar si la expresión contiene varias comparaciones con OR
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
