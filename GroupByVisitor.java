import java.util.stream.Collector;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import sqlitegrammar.*;

public class GroupByVisitor extends SQLiteParserBaseVisitor<String> {
    private StringBuilder textTransformed = new StringBuilder();
    private int resultCounter, columnSize = 0;

    private boolean isWhereExists, isAliasExists, isFromExists, isOrderByExists, isGroupByExists = false;

    @Override
    public String visitSelect_core(SQLiteParser.Select_coreContext ctx) {

        if (ctx.SELECT_() != null && ctx.DISTINCT_() == null) {
            textTransformed.append(ctx.SELECT_().toString() + " ");
        } else {
            textTransformed.append(ctx.SELECT_().toString() + " DISTINCT ");
        }

        if (!ctx.result_column().isEmpty()) {
            columnSize = ctx.result_column().size();
        }
        if (ctx.GROUP_() != null) {
            this.isGroupByExists = true;

        }

        if (ctx.WHERE_() != null) {
            this.isWhereExists = true;
        }

        String result = super.visitSelect_core(ctx);
        return result;
    }

    @Override
    public String visitOrder_by_stmt(SQLiteParser.Order_by_stmtContext ctx) {
        if (ctx.ORDER_() != null && !isGroupByExists) {
            this.isOrderByExists = true;
            this.textTransformed.append(" ORDER BY ");
        }else{
            this.textTransformed.append(ctx.ordering_term() + " ORDER BY " + ctx.ordering_term());
        }

        System.out.println("--------");
        System.out.println(" transfomed text en visit order by");
        System.out.println(textTransformed);
        System.out.println("--------");
        return ctx.getText();
    }

    @Override
    public String visitOrdering_term(SQLiteParser.Ordering_termContext ctx){
        return ctx.getText();
    }
    @Override
    public String visitExpr(SQLiteParser.ExprContext ctx) {
        if (this.isWhereExists) {
            this.textTransformed.append(ctx.getText() + " GROUP BY ");
        }

        return ctx.getText();
    }

    @Override
    public String visitTable_name(SQLiteParser.Table_nameContext ctx) {
        if (isFromExists && !isAliasExists) {
            textTransformed.append(ctx.getText());
        }
        System.out.println("--------");
        System.out.println(" transfomed text en visit Table name");
        System.out.println(textTransformed);
        System.out.println("--------");
        return ctx.getText();
    }

    @Override
    public String visitTable_alias(SQLiteParser.Table_aliasContext ctx) {
        if (isWhereExists) {
            textTransformed.append(" " + ctx.getText() + " WHERE ");
        } else {
            textTransformed.append(" " + ctx.getText() + " GROUP BY ");
        }
        isAliasExists = true;
        System.out.println("--------");
        System.out.println(" transfomed text en visit Table alias");
        System.out.println(textTransformed);
        System.out.println("--------");
        return ctx.getText();
    }

    @Override
    public String visitResult_column(SQLiteParser.Result_columnContext ctx) {
        resultCounter++;
        if (resultCounter < columnSize) {
            textTransformed.append(ctx.getText() + ",");
        } else {
            textTransformed.append(ctx.getText() + " FROM ");
            isFromExists = true;
        }
        return ctx.getText();
    }

    @Override
    public String defaultResult() {
        return this.textTransformed.toString();
    }

}
