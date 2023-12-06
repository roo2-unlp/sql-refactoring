import org.antlr.v4.runtime.ParserRuleContext;
import sqlitegrammar.*;

public class CountFinderVisitor extends SQLiteParserBaseVisitor<Void> {

    private ParserRuleContext countNode = null;
    private HavingAnalyzerForVisitors analyzer = new HavingAnalyzerForVisitors(); // Consideramos un posible HAVING COUNT(*)

    @Override
    public Void visitExpr(SQLiteParser.ExprContext ctx) {
        if (analyzer.existOperatorFromContexts(ctx)) {
            return null;
        }
        if (this.isValidCountExpr(ctx)){
            this.countNode = ctx;       // Capturamos (el nodo completo), una vez dentro de la función count.
        }
        return super.visitExpr(ctx);
    }

    protected boolean isValidCountExpr(SQLiteParser.ExprContext ctx) {  // chequea que el count sea con estrella.
        return  ctx.STAR() != null &&
                ctx.function_name() != null &&
                ctx.function_name().getText().equalsIgnoreCase("COUNT");
    }

    public boolean existCountFunctionWithStar(){ return this.countNode != null;}

}


