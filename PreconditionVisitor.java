import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import sqlitegrammar.*;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;
import java.util.*;

public class PreconditionVisitor extends SQLiteParserBaseVisitor<Boolean> {

    private boolean cumplePrecondicion;

    @Override
    public Boolean visitExpr(SQLiteParser.ExprContext exprContext) {
        if (exprContext.OR_() != null) {
            this.cumplePrecondicion = true;
            for (ParserRuleContext child : exprContext.children()) {
                if (child instanceof TerminalNode && child.symbol() == SQLiteParser.EQ) {
                    // Hay un = en la expresion
                    return true;
                }
            }

            // List<SQLiteParser.ExprContext> comparisons = exprContext.expr();
            // for (SQLiteParser.ExprContext comparison : comparisons) {             
            //     System.out.println("comparison.getText() = " + comparison.getText());
            //     System.out.println("exprContext.EQ() = " + comparison.EQ());
            // }
            return true; // Devuelve true si encuentra OR
        } else {
            this.cumplePrecondicion = false;
            return false; // Devuelve false si no encuentra OR
        }
    }

    public boolean getCumplePrecondicion() {
        return this.cumplePrecondicion;
    }
}