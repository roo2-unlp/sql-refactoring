import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import java.lang.Math.*;
import sqlitegrammar.*;

public class CheckPostCondVisitor extends SQLiteParserBaseVisitor<Void> {
    private ParserRuleContext likeNode = null;

    @Override
    public Void visitExpr(SQLiteParser.ExprContext ctx) {
        if (this.isValidPostConditionExpr(ctx)){
            this.likeNode = ctx;
        }
        return super.visitExpr(ctx);
    }

    protected boolean isValidPostConditionExpr(SQLiteParser.ExprContext ctx){
        if (ctx.LIKE_() != null){ //chqueamos que haya nodo like
            String expr = ctx.getChild(2).getText(); //guardamos texto de la expresion del like en un string
            if (expr.contains("%")){
                int count = Math.toIntExact(expr.chars().filter(ch -> ch == '%').count()); //contamos cant de % en la expresion
                
                if ((count == 1) && (expr.charAt(expr.length()-2) == '%')){ 
                    return true; //si la expr cntiene solo un % y esta al final devuelvo true
                }else{ return false;} //caso contrario, el refactor no funciono y devuelvo true

            }else{ return false;} //en el caso de no encontarnos en el nodo like o que la expr del like no contenga %, devuelvo false
        }else{ return false;}
    }

    public boolean PostConditionsAreMet(){
        return this.likeNode != null;
    }
}
