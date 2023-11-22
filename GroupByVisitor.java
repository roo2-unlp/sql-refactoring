import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;


import sqlitegrammar.*;

public class GroupByVisitor extends SQLiteParserBaseVisitor<String> {

    @Override
    public String visitSelect_core(SQLiteParser.Select_coreContext ctx) {

        
    //    System.out.println(ctx.SELECT_() == null ? "no se encuentra SELECT" : "Se encontro el SELECT");
    //    System.out.print(ctx.result_column().size()==0 ? "no hay expresiones en el selec" : "tiene: ");
    //    System.out.println(ctx.result_column().size());

    //    System.out.print(ctx.groupByExpr.size()==0 ? "no hay expresiones en el group by " : "tiene: ");
    //    System.out.println(ctx.groupByExpr.size());

        //si no encuentro group by tengo que agregarlo
        if (ctx.groupByExpr == null) {            
           return modifiedQuery.append("PEPE").toString();
        }
        
        return ctx.getText();
    }


    @Override
    public String visitParse(SQLiteParser.ParseContext ctx) {
        
        return ctx.getText();
    }

}
