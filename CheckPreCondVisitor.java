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
        //chequeo que estoy en el nodo del like y que la expr contiene %
        if ((ctx.LIKE_().getText().equalsIgnoreCase("LIKE")) && (ctx.getChild(2).getText().contains("%"))){
            //si la expr cntiene el % al final devuelvo false
            System.out.println("string de expr: -1:" + ctx.getChild(2).getText().charAt(-1) + " , -2: " + ctx.getChild(2).getText().charAt(-2));
            int count = Math.toIntExact(ctx.getChild(2).getText().chars().filter(ch -> ch == '%').count());//cuenta cant de % en el string
            
            if ((count == 1) && (ctx.getChild(2).getText().charAt(-1) == '%')){ //si cuenta la "" del string de expr creo que va -2 en luygar de -1
                return false;
            }else{return true;}//caso contrario, hay que refactorizar y devuelvo true
        }else{return false;}//en el caso de no encontarnos en el nodo like o que la expr del like no contenga %, devuelvo false
    }

    public boolean PreConditionsAreMet(){
        return this.likeNode != null;
    }
}
