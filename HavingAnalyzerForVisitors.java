import sqlitegrammar.SQLiteParser;

public class HavingAnalizerForVisitors {

    private boolean hasACountInHaving = false;

    public boolean existOperatorFromContexts(SQLiteParser.ExprContext ctx){
        if (existOperatorForCompare(ctx)){
            return true;
        }
        return false;
    }

    private boolean existOperatorForCompare(SQLiteParser.ExprContext ctx){
        return getByGreaterComparator(ctx) || getByLetterComparator(ctx) || ctx.EQ() != null;
    }

    private boolean getByLetterComparator(SQLiteParser.ExprContext ctx) {
        return ctx.LT() != null || ctx.LT2() != null || ctx.LT_EQ() != null;
    }

    private boolean getByGreaterComparator(SQLiteParser.ExprContext ctx) {
        return ctx.GT() != null || ctx.GT2() != null || ctx.GT_EQ() != null;
    }
}
