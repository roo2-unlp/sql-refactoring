import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import org.antlr.v4.runtime.misc.Interval;
import sqlitegrammar.*;

public class CountVisitor extends SQLiteParserBaseVisitor<String> {

    private String columnName;
    private TokenStreamRewriter rewriter;

    public CountVisitor(CommonTokenStream tokens, String columnName) {
        super();
        this.columnName = columnName;
        this.rewriter = new TokenStreamRewriter(tokens);
    }

    @Override
    public String visitParse(SQLiteParser.ParseContext ctx) {
        this.visitChildren(ctx);
        return rewriter.getText();
    }

    @Override
    public String visitExpr(SQLiteParser.ExprContext ctx) {
        HavingAnalizerForVisitors analizer = new HavingAnalizerForVisitors();
        if (analizer.existOperatorFromContexts(ctx)) {
            return "";
        }
        if (ctx.function_name() == null) { 
            return visitChildren(ctx); 
        }
        if ( ! ctx.function_name().getText().equalsIgnoreCase("COUNT")) { 
            return visitChildren(ctx); 
        }
        if (ctx.STAR() != null) {
            Token starToken = ctx.STAR().getSymbol();
            this.rewriter.replace(starToken.getTokenIndex(), columnName);
        }
        return visitChildren(ctx);
    }
}
