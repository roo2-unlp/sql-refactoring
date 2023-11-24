import java.util.stream.Collector;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import sqlitegrammar.*;

public class GroupByVisitor extends SQLiteParserBaseVisitor<String> {
    private StringBuilder newText = new StringBuilder();
    private int resultCounter, columnSize = 0;

    @Override
    public String visitSelect_core(SQLiteParser.Select_coreContext ctx) {

        //Ver que pasa cuando ya contiene 

        if (ctx.SELECT_() != null) {
            newText.append(ctx.SELECT_().toString() + " ");
        }
        if(!ctx.result_column().isEmpty()){
             columnSize = ctx.result_column().size();            
        }
     
        // if(ctx.result_column().size() == resultCounter) {
        //     System.out.println("FROM AGREGAR");
        //     System.out.println(newText);
        //     System.out.println(resultCounter);
        //     //newText.append(ctx.FROM_().toString());
        // }
        if (ctx.groupByExpr == null) {
            return null;
            // validar que no hay groupy y agregarlo como el resto
        }
        String result = super.visitSelect_core(ctx);

        return result;

    }

    @Override
    public String visitTable_name(SQLiteParser.Table_nameContext ctx) {
        // Ver de sacar el FROM Hardcode y agregarlo con el ctx.FROM_().toString de mas
        // arriba
        newText.append(" FROM " + ctx.getText());
        return ctx.getText();
    }

    @Override
    public String visitTable_alias(SQLiteParser.Table_aliasContext ctx){       
        newText.append(" "+ctx.getText());
        return ctx.getText();
    }

    @Override
    public String visitResult_column(SQLiteParser.Result_columnContext ctx) {
        resultCounter++;
        if (resultCounter < columnSize) {
            newText.append(ctx.getText() + ",");
        }else{
            newText.append(ctx.getText());
        }        
        return ctx.getText();
    }

    @Override
    public String defaultResult() {
        return this.newText.toString();
    }

}
