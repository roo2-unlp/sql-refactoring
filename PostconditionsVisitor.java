import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import sqlitegrammar.*;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

public class PostconditionsVisitor extends SQLiteParserBaseVisitor<Boolean> {
    private boolean cumplePostconditions;

    @Override
    public Boolean visitExpr(SQLiteParser.ExprContext ctx) {
        // Verificar si la expresión contiene varias comparaciones con OR
        System.out.println("------------PostconditionsVisitor--------------");
        System.out.println(" el contendido de ctx  " + ctx.getText());
        System.out.println("el contenido de ctx.IN_() es " + ctx.IN_());
        if (ctx.IN_() != null) {
            System.out.println("estoy en el if de ctx.IN_()");
            this.cumplePostconditions = true;
            return true; // Devuelve true si encuentra OR
        } else {
            System.out.println("estoy en el else de ctx.IN_()");
            this.cumplePostconditions = false;
            return false; // Devuelve false si no encuentra OR
        }
    }

    public boolean getcumplePostconditions() {
        return this.cumplePostconditions;
    }
}
