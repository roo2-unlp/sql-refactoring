import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import sqlitegrammar.*;

public class CountVisitor extends SQLiteParserBaseVisitor<String> {

    @Override
    public String visitParse(SQLiteParser.ParseContext ctx) {
        this.visitChildren(ctx);
        return ctx.getText();
    }
    //NODOS DE INTERES
    @Override
    public String visitExpr(SQLiteParser.ExprContext ctx) {
        //FUNCIONA PARA MAYUSCULAS SOLAMENTE
        if ( ! ctx.getChild(0).getText().equals("COUNT") && ! ctx.getChild(0).getText().equals("count")) { 
            return visitChildren(ctx); 
        }
        for (int i = 1; i < ctx.getChildCount(); i++) {
            System.out.println(ctx.getChild(i).getText());
        }
        return visitChildren(ctx);
    }

    @Override
    public String visitAny_name(SQLiteParser.Any_nameContext ctx) {
        return visitChildren(ctx);
    }

}
