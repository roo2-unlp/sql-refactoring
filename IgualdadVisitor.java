import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import sqlitegrammar.*;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

public class IgualdadVisitor extends SQLiteParserBaseVisitor<Boolean> {
    private boolean visitEQ;
    @Override
    public Boolean visitExpr(SQLiteParser.ExprContext ctx) {
        System.out.println("-----------PRE CONDICION IGUALDAD--------------");

        System.out.println("IgualdadVisitor ctx.EQ()" + ctx.EQ());
        System.out.println("IgualdadVisitor ctx.getText()" + ctx.getText());

        if (ctx.EQ() != null) {
            this.visitEQ = true;
            return true; // Devuelve true si encuentra OR
        } else {
            this.visitEQ = false;
            return false; // Devuelve false si no encuentra OR
        }
    }
    
    public boolean getVisitEQ() {
        return this.visitEQ;
    }
}
