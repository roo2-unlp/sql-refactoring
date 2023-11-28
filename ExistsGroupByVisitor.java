import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import sqlitegrammar.*;


public class ExistsGroupByVisitor extends SQLiteParserBaseVisitor<Boolean> { 
    private Boolean existsGroupBy = false;

    @Override
    public Boolean visitSelect_core(SQLiteParser.Select_coreContext ctx) {

        if(ctx.GROUP_()!=null){
            existsGroupBy = true;
        }
        
        return existsGroupBy;
    }

    @Override
    public Boolean defaultResult() {
        return this.existsGroupBy;
    }
}
