import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import sqlitegrammar.*;


public class CheckPostVistor extends SQLiteParserBaseVisitor<Boolean> { 
    private Boolean existsLimit = false;

    //quien llama a este metodo? 
    public Boolean isValidPost(SQLiteParser.Order_by_stmtContext ctx){
        if(this.visitLimit_stmt(ctx)){
            return true
        }
        return false
    }

    @Override
    public Boolean visitLimit_stmt(SQLiteParser.Limit_stmtContext ctx){
        if(ctx.LIMIT_() != null){
            existsLimit = true;
        }  
        return existsLimit;
    }

}