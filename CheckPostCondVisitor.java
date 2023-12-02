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
    
    // protected boolean isValidPreConditionExpr(SQLiteParser.ExprContext ctx){
    //     if (ctx.LIKE_() != null){   //chqueamos que haya nodo like
    //         //if ((ctx.LIKE_().getText().equalsIgnoreCase("LIKE"))){ //chqueamos que haya nodo like, creo q este if es redundante
    //             String expr = ctx.getChild(2).getText(); //guardamos texto de la expresion del like en un string
    //             if (expr.contains("%")){ //chqueamos que la expr contenga % si existe
    //                 int count = Math.toIntExact(expr.chars().filter(ch -> ch == '%').count()); //contamos cant de % en la expresion
    //                 //System.out.println("count: " + count + ", expr: " + expr + ", expr.lenght: " + expr.charAt(expr.length()-2));

    //                 if ((count == 1) && (expr.charAt(expr.length()-2) == '%')){
    //                     return false;   //la expr se encuantra en forma valida 'ar%' enotnces no hace falta refactorizar este caso
    //                 }else{ return true;}

    //             }else{ return false;}
    //         //}else { return false;}
    //     }else { return false;}
    // }

    protected boolean isValidPostConditionExpr(SQLiteParser.ExprContext ctx){
        //chequeo que estoy en el nodo del like y que la expr contiene %
        if ((ctx.LIKE_().getText().equalsIgnoreCase("LIKE")) && (ctx.getChild(2).getText().contains("%"))){
            String expr = ctx.getChild(2).getText(); //guardamos texto de la expresion del like en un string
            
            //System.out.println("string de expr: -1:" + ctx.getChild(2).getText().charAt(-1) + " , -2: " + ctx.getChild(2).getText().charAt(-2));
            int count = Math.toIntExact(expr.chars().filter(ch -> ch == '%').count()); //contamos cant de % en la expresion
            
            //si la expr cntiene solo un % y esta al final devuelvo true
            if ((count == 1) && (expr.charAt(expr.length()-2) == '%')){ 
                return true;
            }else{return false;}//caso contrario, el refactor no funciono y devuelvo true

        }else{return false;}//en el caso de no encontarnos en el nodo like o que la expr del like no contenga %, devuelvo false
    }

    public boolean PostConditionsAreMet(){
        return this.likeNode != null;
    }
}
