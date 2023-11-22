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
        //chequeo que estoy en el nodo del like y que la expr contiene %
        if ((ctx.LIKE_().getText().equalsIgnoreCase("LIKE")) && (ctx.getChild(2).getText().contains("%"))){

            
            System.out.println("string de expr: -1:" + ctx.getChild(2).getText().charAt(-1) + " , -2: " + ctx.getChild(2).getText().charAt(-2));
            int count = Math.toIntExact(ctx.getChild(2).getText().chars().filter(ch -> ch == '%').count());//cuenta cant de % en el string
            
            //si la expr cntiene solo un % y esta al final devuelvo true
            if ((count == 1) && (ctx.getChild(2).getText().charAt(-1) == '%')){ //si cuenta la "" del string de expr creo que va -2 en luygar de -1
                return true;
            }else{return false;}//caso contrario, el refactor no funciono y devuelvo true

        }else{return false;}//en el caso de no encontarnos en el nodo like o que la expr del like no contenga %, devuelvo false
    }

    public boolean PostConditionsAreMet(){
        return this.likeNode != null;
    }
}
