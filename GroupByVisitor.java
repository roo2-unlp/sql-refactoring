import java.util.stream.Collector;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import sqlitegrammar.*;

public class GroupByVisitor extends SQLiteParserBaseVisitor<String> {
    private StringBuilder textTransformed = new StringBuilder();
    private StringBuilder parameters = new StringBuilder();
    private String whereStatement,fromStatement = "";
    private String groupByStatement=" GROUP BY ";
    private int resultCounter, columnSize = 0;

    private boolean isWhereExists, isAliasExists, isFromExists, isOrderByExists, isGroupByExists = false;

    @Override
    public String visitSelect_core(SQLiteParser.Select_coreContext ctx) {

        if (ctx.SELECT_() != null && ctx.DISTINCT_() == null) {
            textTransformed.append(ctx.SELECT_().toString() + " ");
        } else {
            textTransformed.append(ctx.SELECT_().toString() + " "+ctx.DISTINCT_().toString()+" ");
        }

        if(ctx.FROM_()!=null){
            this.fromStatement=" "+ctx.FROM_().toString()+" ";
        }
        if (!ctx.result_column().isEmpty()) {
            columnSize = ctx.result_column().size();
        }

         if (ctx.WHERE_() != null) {
            this.isWhereExists = true;
            this.whereStatement=" "+ctx.WHERE_().toString()+" ";
        }

        String result = super.visitSelect_core(ctx);
        return result;
    }
    
    
    @Override
    public String visitExpr(SQLiteParser.ExprContext ctx) {
        if (this.isWhereExists) {
            this.textTransformed.append(ctx.getText() + this.groupByStatement);
        }
        return ctx.getText();
    }

    @Override
    public String visitTable_name(SQLiteParser.Table_nameContext ctx) {
        if (isFromExists && !isAliasExists) {
            textTransformed.append(ctx.getText());  
        }       
        return ctx.getText();
    }

    @Override
    public String visitTable_alias(SQLiteParser.Table_aliasContext ctx) {
        if (isWhereExists) {
            textTransformed.append(" " + ctx.getText() + this.whereStatement);
        } else {
            textTransformed.append(" " + ctx.getText() + this.groupByStatement);
        }
        isAliasExists = true;       
        return ctx.getText();
    }

    @Override
    public String visitResult_column(SQLiteParser.Result_columnContext ctx) {
        resultCounter++;
        if (resultCounter < columnSize) {            
            textTransformed.append(ctx.getText() + ",");
        } else {
            textTransformed.append(ctx.getText() + this.fromStatement);           
            isFromExists = true;
        }
        return ctx.getText();
    }

    @Override
    public String defaultResult() {
        return this.textTransformed.toString();
    }

}
