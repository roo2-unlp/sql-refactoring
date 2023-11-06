import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import sqlitegrammar.*;

public class LikeVisitor extends SQLiteParserBaseVisitor<String> {

    @Override
    public String visitParse(SQLiteParser.ParseContext ctx) {
        this.visitChildren(ctx);
        return ctx.getText();
    }

    //entro al nodo q me interesa (se hace de esta manera?)
    public String visitLiteral_value(SQLiteParser.ExprContext ctx) {
        if (ctx.getChild(1).getText().equals("LIKE") || ctx.getChild(1).getText().equals("like")) {
            this.executeAction(ctx.getChild(2)); //cuando encuentro el nodo, realizo el refactor
        }
        return visitChildren(ctx);
    }
 
        public String executeAction(SQLiteParser.ParseContext ctx){ 
        String value = ctx.getText(); // tomo el texto
        String refactoredValue = value.replace("%", ""); // le saco todos los %
        return refactoredValue.concat("%"); // le agrego un unico % al final y se lo devuelvo
    }
}
