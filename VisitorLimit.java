import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import sqlitegrammar.*;


public class VisitorLimit extends SQLiteParserBaseVisitor<Boolean> { 
    private Boolean existsLimit = false;

    //quien llama a este metodo? 
    public Boolean isValidLimit(){
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