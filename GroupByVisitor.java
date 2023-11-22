import java.util.stream.Collector;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import sqlitegrammar.*;

public class GroupByVisitor extends SQLiteParserBaseVisitor<String> {

    @Override
    public String visitSelect_core(SQLiteParser.Select_coreContext ctx) {

        
        // Deberia validar aca si no encuentro el groupBY y devolver un boolean?
        if (ctx.groupByExpr == null) {
            // o Aca deberia hacer el agregado del groupBy?
        } else {
            System.out.println("SQL : ");            
            System.out.println(ctx.SELECT_().toString() + " ");
            System.out.println("Variables dentro del select: ");
            System.out.println(ctx.result_column().size());
            System.out.println("_____");
            //result_column().stream().forEach(sel -> System.out.println(sel.column_alias().toString()));
            
        }
        // Esto es correcto sobre lo que deberiamos devolver?
        return ctx.SELECT_().toString() + ctx.FROM_().toString() + ctx.groupByExpr.toString();
    }

    // @Override
    // public String visitParse(SQLiteParser.ParseContext ctx) {
    //     // Hay que buscar la forma de parsear sin los espacios
    //     return ctx.getText();
    // }

}
