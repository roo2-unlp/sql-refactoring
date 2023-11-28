import java.util.stream.Collector;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import sqlitegrammar.*;

public class GroupByVisitor extends SQLiteParserBaseVisitor<String> {
    private StringBuilder newText = new StringBuilder();
    private int resultCounter, columnSize = 0;
    // Dummy validations
    //VALIDAR SI LA LOGICA DE LA TRANSFORMACION ES LA CORRECTA O TENDRIA QUE HACERLO DESDE OTRO LADO?
    private boolean isWhereExists,isAliasExists, isFromExists, isGroupByExists = false;

    @Override
    public String visitSelect_core(SQLiteParser.Select_coreContext ctx) {

        if (ctx.SELECT_() != null) {
            newText.append(ctx.SELECT_().toString() + " ");
        }
        if (!ctx.result_column().isEmpty()) {
            columnSize = ctx.result_column().size();
        }  
        //VALIDAR SI PUEDO USARLO SOLO CUANDO HAGO LA TRANSFORMACION Y VERIFICAR DESDE EL PRECONDITION PARA NO TENER QUE HACER NADA SI TENGO EL GROUP BY 
        //CON SUS RESPECTIVAS VARIABLES, TAMBIEN PREGUNTAR ESO, COMO VALIDAR LAS VARIABLES DESDE EL PRECONDITION  
        // if (ctx.GROUP_() != null) {            
        //     isGroupByExists =true;            
        // }
        if(ctx.WHERE_()!=null){
            this.isWhereExists=true;
        }

        String result = super.visitSelect_core(ctx);
        return result;
    }


    public String visitExpr(SQLiteParser.ExprContext ctx) { 
        if(this.isWhereExists){
            this.newText.append(ctx.getText() + " GROUP BY ");
        }
        return ctx.getText();    
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
        if(isWhereExists) {            
             newText.append(" "+ctx.getText()+" WHERE ");             
        } else{
            newText.append(" "+ctx.getText()+" GROUP BY ");
        }      
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
