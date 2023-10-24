import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import sqlitegrammar.*;

public class CountVisitor extends SQLiteParserBaseVisitor<String> {

    @Override
    public String visitParse(SQLiteParser.ParseContext ctx) {
        return ctx.getText();
    }

    @Override
    public String visitExpr(SQLiteParser.ExprContext ctx) {
        System.out.println("expr_nodo");
        return visitChildren(ctx);
    }

    @Override
    public String visitAny_name(SQLiteParser.Any_nameContext ctx) {
        System.out.println("any_name_nodo");
        return visitChildren(ctx);
    }

}
