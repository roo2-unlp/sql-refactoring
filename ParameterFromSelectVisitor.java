import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import sqlitegrammar.*;


public class ParameterFromSelectVisitor extends SQLiteParserBaseVisitor<String> { 
    private StringBuilder resultColumn = new StringBuilder() ;
    private int resultCounter, columnSize = 0;

    @Override
    public String visitSelect_core(SQLiteParser.Select_coreContext ctx) {
        if (!ctx.result_column().isEmpty()) {
            columnSize = ctx.result_column().size();
        } 
        String result = super.visitSelect_core(ctx);
        return result;
    }

    @Override
    public String visitResult_column(SQLiteParser.Result_columnContext ctx) {        
        resultCounter++;       
        if (resultCounter < columnSize) {
            resultColumn.append(ctx.getText() + ",");
        } else {
            this.resultColumn.append(ctx.getText());
        }
        return ctx.getText();
    }

    @Override
    public String defaultResult() {
        return this.resultColumn.toString();
    }
}


