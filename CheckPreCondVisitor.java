import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import java.lang.Math.*;
import sqlitegrammar.*;

public class CheckPreCondVisitor extends SQLiteParserBaseVisitor<Void> {
    private ParserRuleContext likeNode = null;

    @Override
    public Void visitExpr(SQLiteParser.ExprContext ctx) {
        if (this.isValidPreConditionExpr(ctx)){
            this.likeNode = ctx;
        }
        return super.visitExpr(ctx);
    }


    protected boolean isValidPreConditionExpr(SQLiteParser.ExprContext ctx){
        if (ctx.LIKE_() != null){   //chqueamos que haya nodo like
            //if ((ctx.LIKE_().getText().equalsIgnoreCase("LIKE"))){ //chqueamos que haya nodo like, creo q este if es redundante
                String expr = ctx.getChild(2).getText(); //guardamos texto de la expresion del like en un string
                if (expr.contains("%")){ //chqueamos que la expr contenga % si existe
                    int count = Math.toIntExact(expr.chars().filter(ch -> ch == '%').count()); //contamos cant de % en la expresion
                    //System.out.println("count: " + count + ", expr: " + expr + ", expr.lenght: " + expr.charAt(expr.length()-2));

                    if ((count == 1) && (expr.charAt(expr.length()-2) == '%')){
                        return false;   //la expr se encuantra en forma valida 'ar%' enotnces no hace falta refactorizar este caso
                    }else{ return true;}

                }else{ return false;}
            //}else { return false;}
        }else { return false;}
    }

    public boolean PreConditionsAreMet(){
        return this.likeNode != null;
    }
}
