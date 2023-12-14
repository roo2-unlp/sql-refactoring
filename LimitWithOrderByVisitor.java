import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import sqlitegrammar.*;
import org.antlr.v4.runtime.tree.ParseTree;

public class LimitWithOrderByVisitor extends SQLiteParserBaseVisitor<String> {

    private StringBuilder transformedText = new StringBuilder();
    private int limit = 10;

    @Override
    public String visitSelect_stmt(SQLiteParser.Select_stmtContext ctx) {
        /*if (ctx.limit_stmt() != null) {
            // Si ya hay una cláusula LIMIT, retornamos el texto de select_core
           return transformedText.append(
                ctx.select_core(0).getChild(0).getText() + " " +
                ctx.select_core(0).getChild(1).getText() + " " +
                ctx.select_core(0).getChild(2).getText() + " " +
                ctx.select_core(0).getChild(3).getText() + " " +
                ctx.order_by_stmt().getChild(0).getText() + " " +
                ctx.order_by_stmt().getChild(1).getText() + " " +
                ctx.order_by_stmt().getChild(2).getText() + " " +
                ctx.limit_stmt().getChild(0).getText() + " " +
                ctx.limit_stmt().getChild(1).getText() + " " +
                ";"
        ).toString();
        }
        // Si no hay una cláusula LIMIT, la agregamos
        return transformedText.append(
                ctx.select_core(0).getChild(0).getText() + " " +
                ctx.select_core(0).getChild(1).getText() + " " +
                ctx.select_core(0).getChild(2).getText() + " " +
                ctx.select_core(0).getChild(3).getText() + " " +
                ctx.order_by_stmt().getChild(0).getText() + " " +
                ctx.order_by_stmt().getChild(1).getText() + " " +
                ctx.order_by_stmt().getChild(2).getText() + " " +
                " " + " LIMIT " + limit + ";").toString();*/

            if (ctx.limit_stmt() != null) {
            // Si ya hay una cláusula LIMIT, retornamos el texto de select_core
            
            for (int i = 0; i < ctx.select_core(0).getChildCount(); i++) {
                transformedText.append(ctx.select_core(0).getChild(i).getText()).append(" ");
            }

            for (int i = 0; i < ctx.order_by_stmt().getChildCount(); i++) {
                transformedText.append(ctx.order_by_stmt().getChild(i).getText()).append(" ");
            }

            for (int i = 0; i < ctx.limit_stmt().getChildCount(); i++) {
                transformedText.append(ctx.limit_stmt().getChild(i).getText()).append(" ");
            }

            transformedText.append(";");
            return transformedText.toString();
        }

        // Si no hay una cláusula LIMIT, la agregamos
         for (int i = 0; i < ctx.select_core(0).getChildCount(); i++) {
                transformedText.append(ctx.select_core(0).getChild(i).getText()).append(" ");
            }

        for (int i = 0; i < ctx.order_by_stmt().getChildCount(); i++) {
            transformedText.append(ctx.order_by_stmt().getChild(i).getText()).append(" ");
        }

        transformedText.append(" LIMIT ").append(limit).append(";");
        return transformedText.toString();
        }
    

    @Override
    protected String defaultResult() {
        return transformedText.toString();
    }

    @Override
    protected String aggregateResult(String aggregate, String nextResult) {
        return aggregate + " " + nextResult;
    }
    

    public void setLimit(int l){
        this.limit = l;
    }
}