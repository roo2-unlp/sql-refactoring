import java.util.stream.Collector;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import sqlitegrammar.*;

public class GroupByVisitor extends SQLiteParserBaseVisitor<String> {
    private StringBuilder newText = new StringBuilder();

    @Override
    public String visitSelect_core(SQLiteParser.Select_coreContext ctx) {

        if (ctx.SELECT_() != null) {
            newText.append(ctx.SELECT_().toString() + " ");
        }
        // if(ctx.FROM_() != null) {
        // newText.append(ctx.FROM_().toString());
        // }
        if (ctx.groupByExpr == null) {
            //validar que no hay groupby y agregarlo como el resto
        } 
        String result = super.visitSelect_core(ctx);
        return result;        

    }

    @Override
    public String visitTable_name(SQLiteParser.Table_nameContext ctx) {
        //Ver de sacar el FROM Hardcode y agregarlo con el ctx.FROM_().toString de mas arriba
        newText.append(" FROM "+ctx.getText()+ " ");
        return ctx.getText();
    }
    @Override
    public String visitTable_alias(SQLiteParser.Table_aliasContext ctx){
        newText.append(ctx.getText() );
        return ctx.getText();
    }  
    
    @Override
    public String visitResult_column(SQLiteParser.Result_columnContext ctx){        
        newText.append(ctx.getText()+",");
        return ctx.getText();
    }


    @Override
    public String defaultResult() {
        return this.newText.toString();
    }


}
