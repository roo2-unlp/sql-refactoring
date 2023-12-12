import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import sqlitegrammar.*;
import org.antlr.v4.runtime.tree.ParseTree;

public class LimitWithOrderByVisitor extends SQLiteParserBaseVisitor<String> {

    private StringBuilder transformedText = new StringBuilder();
    private int limit = 10;

    @Override
    public String visitSelect_stmt(SQLiteParser.Select_stmtContext ctx) {
        if (ctx.limit_stmt() != null) {
            // Si ya hay una cláusula LIMIT, retornamos el texto de select_core
           return transformedText.append(ctx.select_core(0).getText() + ctx.order_by_stmt().getText() + " " + ctx.limit_stmt().getText() + ";").toString();
        }
        // Si no hay una cláusula LIMIT, la agregamos
        return transformedText.append(ctx.select_core(0).getText() + " " + ctx.order_by_stmt().getText() + " LIMIT " + limit + ";").toString();
    }

    @Override
    protected String defaultResult() {
        return transformedText.toString();
    }

    /*
    @Override
    protected String aggregateResult(String aggregate, String nextResult) {
        return aggregate + " " + nextResult;
    }
    */

    public void setLimit(int l){
        this.limit = l;
    }
}