import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;


import sqlitegrammar.*;

public class GroupByVisitor extends SQLiteParserBaseVisitor<String> {

    private StringBuilder modifiedQuery = new StringBuilder();  

    @Override
    public String visitSelect_core(SQLiteParser.Select_coreContext ctx) {

        
       System.out.println(ctx.SELECT_() == null ? "no se encuentra SELECT" : "Se encontro el SELECT");

       System.out.print(ctx.groupByExpr.size()==0 ? "no hay expresiones en el group by " : "tiene: ");
       System.out.println(ctx.groupByExpr.size());

        System.out.print(ctx.result_column().size()==0 ? "no hay expresiones en el selec" : "tiene: ");
       System.out.println(ctx.result_column().size());
   
        // if (ctx.BY_() != null) {
        //     System.out.println("encontre el BY");
        // }

        // if (ctx.SELECT_() == null || ctx.expr().isEmpty()) {
        //     return false;
        // }

        // if (ctx.groupByExpr != null) {
        //     System.out.println("encontre el group by");
        // } else {
        //     System.out.println("hay un error o no se encontro");
        // }
        // super.visitSelect_core(ctx);

        //si no encuentro group by tengo que agregarlo
        if (ctx.groupByExpr == null) {            
           return modifiedQuery.append("PEPE").toString();
        }
        //super.visitSelect_core(ctx);
        return ctx.getText();
    }

    
    // @Override
    // public String visitParse(SQLiteParser.ParseContext ctx) {
        
    //     return ctx.getText();
    // }

    public String getModifiedQuery(){
        return modifiedQuery.toString();
    }

}
