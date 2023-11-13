import org.antlr.v4.runtime.ParserRuleContext;
import sqlitegrammar.*;

public class CountFinderVisitor extends SQLiteParserBaseVisitor<Void> {
    private ParserRuleContext countNode = null;

    @Override
    public Void visitExpr(SQLiteParser.ExprContext ctx) {
        // Verifica si el nodo actual es una función de agregación "COUNT".
        // && !ctx.getText().contains("HAVING")) Hay que considerar un posible HAVING COUNT(*)
        if (ctx.function_name() != null && this.isValidCountExpr(ctx) && !ctx.getText().contains("HAVING")){    //Che
            this.countNode = ctx;
            // Capturamos (el nodo completo), una vez dentro de la función count.
        }
        return super.visitExpr(ctx);
    }

    protected boolean isValidCountExpr(SQLiteParser.ExprContext ctx) {  // chequea que el count sea con estrella.
        return ctx.function_name().getText().equalsIgnoreCase("COUNT") &&
                ctx.getChild(2).getText().equals("*");
    }

    public boolean existCountFunctionWithStar(){ return this.countNode != null;}

}


