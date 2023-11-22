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

        //Deberia validar aca si no encuentro el groupBY y devolver un boolean? 
        if (ctx.groupByExpr == null) {            
           //o Aca deberia hacer el agregado del groupBy?
        }
        //Esto es correcto sobre lo que deberiamos devolver?
        return ctx.SELECT_().toString()+ ctx.FROM_().toString() + ctx.groupByExpr.toString();
    }


    @Override
    public String visitParse(SQLiteParser.ParseContext ctx) {
        //Hay que buscar la forma de parsear sin los espacios
        return ctx.getText();
    }

}
