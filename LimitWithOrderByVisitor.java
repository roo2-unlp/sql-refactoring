import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import sqlitegrammar.*;
import org.antlr.v4.runtime.tree.ParseTree;

public class LimitWithOrderByVisitor extends SQLiteParserBaseVisitor<String> {

    private StringBuilder transformedText = new StringBuilder();
    private int limit = 10;

    @Override
    public String visitSelect_core(SQLiteParser.Select_coreContext ctx) {
        if (ctx.SELECT_() != null) {
            transformedText.append(ctx.SELECT_().toString()).append(" ");
        }

        if (!ctx.result_column().isEmpty()) {
            for (SQLiteParser.Result_columnContext resultColumn : ctx.result_column()) {
                transformedText.append(resultColumn.toString()).append(" ");
            }
        }

        if (ctx.FROM_() != null) {
            transformedText.append(ctx.FROM_().toString()).append(" ");
        }

        if (ctx.WHERE_() != null) {
            transformedText.append(ctx.WHERE_().toString()).append(" ");
        }

        if (ctx.ORDER_() != null) {
            transformedText.append(ctx.ORDER_().toString()).append(" ");
        }

        if (ctx.BY_() != null) {
            transformedText.append(ctx.BY_().toString()).append(" ");
        }

        if (ctx.LIMIT_() == null) {
            transformedText.append("LIMIT ").append(limit).append(";");
        }

        return transformedText.toString();
    }


    @Override
    protected String defaultResult() {
        return transformedText.toString();
    }

    @Override
    protected String aggregateResult(String aggregate, String nextResult) {
        return aggregate + " " + nextResult;
    }

    public void setLimit(int l){
        this.limit = l;
    }
}