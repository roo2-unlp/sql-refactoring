import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import sqlitegrammar.*;


public class ExistsOrderByVisitor extends SQLiteParserBaseVisitor<Boolean> { 
    private Boolean existsOrderBy = false;

    @Override
    public Boolean visitSelect_core(SQLiteParser.Select_coreContext ctx) {

        if(ctx.ORDER() != null){
            existsOrderBy = true;
        }
        
        return existsOrderBy;
    }

    @Override
    public Boolean defaultResult() {
        return this.existsOrderBy;
    }
}