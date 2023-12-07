import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import sqlitegrammar.*;


public class CheckPostVistor extends SQLiteParserBaseVisitor<Boolean> { 
    private Boolean existsLimit = false;

    //quien llama a este metodo? 
    public Boolean isValidPost(){
        return existsLimit;
    }

    @Override
    public Boolean visitLimit_stmt(SQLiteParser.Limit_stmtContext ctx){
        if(ctx.LIMIT_() != null){
            existsLimit = true;
        }  
        return existsLimit;
    }

}