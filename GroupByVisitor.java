import java.util.stream.Collector;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import sqlitegrammar.*;

public class GroupByVisitor extends SQLiteParserBaseVisitor<String> {
    private StringBuilder newText = new StringBuilder();
    private int resultCounter, columnSize = 0;
    // Dummy validations
    private boolean isWhereExists,isAliasExists, isFromExists, isGroupByExists = false;

    @Override
    public String visitSelect_core(SQLiteParser.Select_coreContext ctx) {

        if (ctx.SELECT_() != null) {
            newText.append(ctx.SELECT_().toString() + " ");
        }
        if (!ctx.result_column().isEmpty()) {
            columnSize = ctx.result_column().size();
        }    
        if (ctx.groupByExpr == null) {
            return null;
            
        }
        String result = super.visitSelect_core(ctx);

        return result;

    }

    @Override
    public String visitTable_name(SQLiteParser.Table_nameContext ctx) {
       if(isFromExists && !isAliasExists){
        newText.append(ctx.getText());
       }
        
        return ctx.getText();
    }

    @Override
    public String visitTable_alias(SQLiteParser.Table_aliasContext ctx) {
        newText.append(" " + ctx.getText()+ " GROUP BY ");
        isAliasExists=true;
        isGroupByExists=true;
        
        return ctx.getText();
    }

    @Override
    public String visitResult_column(SQLiteParser.Result_columnContext ctx) {
       resultCounter++;       
        if (resultCounter < columnSize) {
            newText.append(ctx.getText() + ",");
        } else {
            newText.append(ctx.getText() + " FROM ");
            isFromExists = true;            
        }
        return ctx.getText();
    }

    @Override
    public String defaultResult() {
        return this.newText.toString();
    }

}
