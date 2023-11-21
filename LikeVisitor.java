import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
//import sqlitegrammar.*;

public class LikeVisitor extends SQLiteParserBaseVisitor<String> {

    @Override
    public String visitParse(SQLiteParser.ParseContext ctx) {
        this.visitChildren(ctx);
        return ctx.getText();
    }

    //entro al nodo q me interesa 
    public String visitExpr(SQLiteParser.ExprContext ctx) {
        System.out.println("Entre a la expresion "+ ctx.LIKE_());
        if((ctx.LIKE_().equals("LIKE")) || ctx.LIKE_().equals("like")){ //si estamos en una expresion like, tomo el valor del hijo derecho
            this.executeAction(ctx.getChild(2));
            // return this.executeAction(ctx.getChild(2)) ????
        }
        return visitChildren(ctx);
    }
 
   public String executeAction(SQLiteParser.ParseTree ctx){ 
        String value = ctx.getText(); // tomo el texto
        String refactoredValue = value.replace("%", ""); // le saco todos los %
        return refactoredValue.concat("%"); // le agrego un unico % al final y se lo devuelvo
    }
}