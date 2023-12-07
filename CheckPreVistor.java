import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import sqlitegrammar.*;


public class CheckPrePostVistor extends SQLiteParserBaseVisitor<Boolean> { 
    private Boolean existsOrderBy = false;

    //podemos llamar a este metodo en algun lado? 
    public Boolean isValidPre(){
        return existsOrderBy;
    }

    @Override
    public Boolean visitOrder_by_stmt(SQLiteParser.Order_by_stmtContext ctx) {
        if (ctx.ORDER_() != null && ctx.BY_() != null) {
            existsOrderBy = true;
        }  
        return existsOrderBy;
    }

}