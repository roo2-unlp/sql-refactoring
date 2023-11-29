import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import sqlitegrammar.*;
import org.antlr.v4.runtime.tree.ParseTree;

public class LimitWithOrderByVisitor extends SQLiteParserBaseVisitor<String> {

    private StringBuilder transformedText = new StringBuilder();
    private String result;

    @Override
    public String visitLimit_stmt(SQLiteParser.Limit_stmtContext ctx) {
        if(ctx.LIMIT != null){
            return ctx
        }
        return result.append(ctx.SELECT_().toString() + "LIMIT 10;");
    }

    @Override
    public String visitOrder_by_stmt(SQLiteParser.Order_by_stmtContext ctx) {
        if(ctx.ORDER() != null){
            return result.append(ctx.SELECT_().toString() + ";");
            //preguntar como hacer para que nos devuelva la consulta entera 
        }
        return ctx;    
    }

    // tendiramos que agregar el visitor que verifica si existe o no la columna o ordenar ?
}