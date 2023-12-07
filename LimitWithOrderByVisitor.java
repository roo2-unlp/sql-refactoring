import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import sqlitegrammar.*;
import org.antlr.v4.runtime.tree.ParseTree;

public class LimitWithOrderByVisitor extends SQLiteParserBaseVisitor<String> {

    private StringBuilder transformedText = new StringBuilder();
    private String result;
    private int limit = 10;

    @Override
    public String visitLimit_stmt(SQLiteParser.Limit_stmtContext ctx) {
        if(ctx.LIMIT_() != null){
            return ctx
        }
        return result.append(ctx.SELECT_().toString() + "LIMIT " + limit + ";");
    }

    @Override
    protected String defaultResult() {
        return "";
    }

    @Override
    protected String aggregateResult(String aggregate, String nextResult) {
        return aggregate + " " + nextResult;
    }

    public int setLimit(int l){
        this.limit = l;
    }

    // tendiramos que agregar el visitor que verifica si existe o no la columna o ordenar ?
}