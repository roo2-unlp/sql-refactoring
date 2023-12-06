import sqlitegrammar.*;

public class HavingAnalyzerForVisitors {

    public boolean existOperatorFromContexts(SQLiteParser.ExprContext ctx){
        if (existOperatorForCompare(ctx)){
            return true;
        }
        return false;
    }

    private boolean existOperatorForCompare(SQLiteParser.ExprContext ctx){
        return getByGreaterComparator(ctx) || getByLetterComparator(ctx) || getByDistinctComparator(ctx);
    }

    private boolean getByLetterComparator(SQLiteParser.ExprContext ctx) {
        return ctx.LT() != null || ctx.LT2() != null || ctx.LT_EQ() != null;
    }

    private boolean getByGreaterComparator(SQLiteParser.ExprContext ctx) {
        return ctx.GT() != null || ctx.GT2() != null || ctx.GT_EQ() != null;
    }
     
    private boolean getByDistinctComparator(SQLiteParser.ExprContext ctx) {
        return ctx.EQ() != null || ctx.NOT_EQ1() != null || ctx.NOT_EQ2() != null;
    }
}
