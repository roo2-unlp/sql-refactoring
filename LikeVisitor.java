import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
//import sqlitegrammar.*;

public class LikeVisitor extends SQLiteParserBaseVisitor<String> {

    private String refactoredValue=null;
    //entro al nodo q me interesa 
    public String visitExpr(SQLiteParser.ExprContext ctx) {
        if (ctx.LIKE_() != null){
            if ((ctx.LIKE_().getText().equalsIgnoreCase("LIKE"))){//si estamos en una expresion like, tomo el valor del hijo derecho, 
                                                                //agregue el getText xq sino no entraba cuando se cumplia la condicion y cambie el OR por el equalsIgnoreCase
                System.out.println("hasta ahora el texto es: "+ctx.getText());
                System.out.println("el hijo derecho es:"+ctx.getChild(2).getText());
                String value = ctx.getChild(2).getText(); // tomo el texto
                this.refactoredValue = value.replace("%", ""); // le saco todos los %
                this.refactoredValue = this.refactoredValue.replace("'", ""); // le saco todos los ''
                this.refactoredValue= this.refactoredValue+"%"; // le agrego un unico % al final y se lo devuelvo
                System.out.println("refactored value seria: "+ctx.getChild(0).getText()+" "+ctx.getChild(1).getText()+" "+"'"+this.refactoredValue+"'");
                this.refactoredValue= ctx.getChild(0).getText()+" "+ctx.getChild(1).getText()+" "+"'"+this.refactoredValue+"'"; // le agrego los ''
                //puede haber mas de 3 hijos? (habria q hacer un for q agregue cada hijo con ' ' en medio de cada uno)
                //cumpliria en todos los casos de los test? o solo en los quje son consultas sql correctas?
            }
        }
        return this.refactoredValue;
    }

    public String aggregateResult(String aggregate, String nextResult) {
        System.out.println("texto del agregate es: "+aggregate);
        System.out.println("texto del nextResult es: "+nextResult);
        if (this.refactoredValue != null){
            System.out.println("texto del refactoredValue es: "+this.refactoredValue);
            return this.refactoredValue;
        }else
        return null;
    }
    
    /* en teoria tenemos que redefinir este metodo, pero no logre hacerlo, lo redefino pero no entra nunca a hacer el print
	public String visitChildren(SQLiteParser.ExprContext node) {
        System.out.println("texto del visitChildren es: "+node.getText());
		String result = defaultResult();
		int n = node.getChildCount();
		for (int i=0; i<n; i++) {
			if (!shouldVisitNextChild(node, result)) {
                result = this.aggregateResult(result, "");
				break;
			}else{
                ParseTree c = node.getChild(i);
			    String childResult = c.accept(this);
                System.out.println("childresult es: "+childResult);
            }
		}
		return result;
	}

    protected boolean shouldVisitNextChild(SQLiteParser.ExprContext node, String currentResult) {
        if (node.LIKE_().getText().equalsIgnoreCase("LIKE")){   //creo q habria q poner el valor en una variable priv y cuando entra al nodo like q se setee en falso
		    return false;   //y aca devolver el vaor de la variable
        }else
            return true;    //y aca devolver el vaor de la variable
	} */

}
