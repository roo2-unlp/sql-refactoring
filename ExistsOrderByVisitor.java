import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import sqlitegrammar.*;


public class ExistsOrderByVisitor extends SQLiteParserBaseVisitor<Boolean> { 
   
    private Boolean isExistsOrderBy = false;
    @Override
    public Boolean visitOrder_by_stmt(SQLiteParser.Order_by_stmtContext ctx) {
        if (ctx.ORDER_() != null) {            
            this.isExistsOrderBy=true;
        }
       
        Boolean result = super.visitOrder_by_stmt(ctx);
        return result;
    }


    @Override
    public Boolean defaultResult() {
        return this.isExistsOrderBy;
    }
}