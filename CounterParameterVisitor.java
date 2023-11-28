
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import sqlitegrammar.*;


public class CounterParameterVisitor extends SQLiteParserBaseVisitor<Integer>{
    private int columnSize = 0;
    
    @Override
    public Integer visitSelect_core(SQLiteParser.Select_coreContext ctx) {

        if (!ctx.result_column().isEmpty()) {
            columnSize = ctx.result_column().size();
        }  
        
        return columnSize;
    }

    @Override
    public Integer defaultResult() {
        return this.columnSize;
    }
}
