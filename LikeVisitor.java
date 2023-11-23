import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
//import sqlitegrammar.*;

public class LikeVisitor extends SQLiteParserBaseVisitor<String> {

    @Override
    //entro al nodo q me interesa 
    public String visitExpr(SQLiteParser.ExprContext ctx) {
        //System.out.println("Entre a la expresion "+ ctx.LIKE_().getClass() + " , " + ctx.LIKE_().getText());
        if (ctx.LIKE_() != null){
            if ((ctx.LIKE_().getText().equalsIgnoreCase("LIKE"))){//si estamos en una expresion like, tomo el valor del hijo derecho, 
                                                                //agregue el getText xq sino no entraba cuando se cumplia la condicion y cambie el OR por el equalsIgnoreCase
                //this.executeAction(ctx.getChild(2));
                // return this.executeAction(ctx.getChild(2)) ????
                //System.out.println("entre, tipo de child: "+ ctx.getChild(2).getClass());
                System.out.println("el hijo derecho es:"+ctx.getChild(2).getText());
                String value = ctx.getChild(2).getText(); // tomo el texto
                String refactoredValue = value.replace("%", ""); // le saco todos los %
                refactoredValue = refactoredValue.replace("'", ""); // le saco todos los ''
                refactoredValue= refactoredValue+"%"; // le agrego un unico % al final y se lo devuelvo
                refactoredValue= "'"+refactoredValue+"'"; // le agrego los ''
                System.out.println("refactored value " + refactoredValue);
                //ctx.getChild(2).setState(refactoredValue);
                //return refactoredValue;?
            }
        }
        return super.visitExpr(ctx);
    }
 
    //@Override
    //protected String aggregateResult(T aggregate, T nextResult) {     //hay que poner aca el refactored value?
	// 	return nextResult;
	// }


    // public String executeAction(SQLiteParser.ParseTree ctx){ 
    //     String value = ctx.getText(); // tomo el texto
    //     String refactoredValue = value.replace("%", ""); // le saco todos los %
    //     return refactoredValue.concat("%"); // le agrego un unico % al final y se lo devuelvo
    // }
}
