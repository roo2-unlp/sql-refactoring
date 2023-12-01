import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import sqlitegrammar.*;

public class GetOrderByVisitor extends SQLiteParserBaseVisitor<String> {    
    private StringBuilder parameters = new StringBuilder();
    private static String orderByClause = " ORDER BY ";
    private boolean isExistsOrderBy = false;
    private int resultCounter, columnSize = 0;

    @Override
    public String visitOrder_by_stmt(SQLiteParser.Order_by_stmtContext ctx) {
        if (ctx.ORDER_() != null) {
            this.isExistsOrderBy = true;
            this.parameters.append(orderByClause);
        }
        if (!ctx.ordering_term().isEmpty()) {
            this.columnSize = ctx.ordering_term().size();
        }

        String result = super.visitOrder_by_stmt(ctx);
        return result;
    }

    @Override
    public String visitOrdering_term(SQLiteParser.Ordering_termContext ctx) {
        resultCounter++;
        if (resultCounter < columnSize) {
            this.parameters.append(ctx.getText() + ",");
        } else {
            this.parameters.append(ctx.getText());
        }        
        return ctx.getText();
    }

    @Override
    public String defaultResult() {
        return this.parameters.toString();
    }
}