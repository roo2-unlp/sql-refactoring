import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import sqlitegrammar.*;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

public class PostconditionsVisitor extends SQLiteParserBaseVisitor<Boolean> {
    private boolean cumplePostconditions;

    @Override
    public Boolean visitExpr(SQLiteParser.ExprContext ctx) {
        // Verificar si la expresión contiene varias comparaciones con OR
        if ((ctx.IN_() != null) && (ctx.OR_() == null)) {
            this.cumplePostconditions = true;
            return true; // Devuelve true si encuentra OR
        } else {
            this.cumplePostconditions = false;
            return false; // Devuelve false si no encuentra OR
        }
    }

    public boolean getcumplePostconditions() {
        return this.cumplePostconditions;
    }
}
