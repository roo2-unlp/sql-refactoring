import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import org.antlr.v4.runtime.Token;
//import sqlitegrammar.*;

public class LikeVisitor extends SQLiteParserBaseVisitor<String> {
    private TokenStreamRewriter rewriter;

    //constructor para inicializar el rewriter de Antl
    public LikeVisitor(CommonTokenStream tokens){
        super();
        this.rewriter = new TokenStreamRewriter(tokens);
    }

    //lo modificamos para que devuelva el texto directamente
    public String visitParse(SQLiteParser.ParseContext ctx) {
        this.visitChildren(ctx);
        return this.rewriter.getText();
    }

    public String CleanLikeContent(String value){
        String refactoredValue;
        refactoredValue = value.replace("%", ""); // le saco todos los %
        refactoredValue = refactoredValue.replace("'", ""); // le saco todos los ''
        refactoredValue= refactoredValue+"%"; // le agrego un unico % al final y se lo devuelvo
        refactoredValue = "'"+refactoredValue+"'"; // le agrego los ''
        return refactoredValue;
    }

    //entro al nodo q me interesa 
    public String visitExpr(SQLiteParser.ExprContext ctx) {
        if (ctx.LIKE_() != null){
            if ((ctx.LIKE_().getText().equalsIgnoreCase("LIKE"))){//si estamos en una expresion like, tomo el valor del hijo derecho, 
                //System.out.println("hasta ahora el texto es: "+ctx.getText());
                //System.out.println("el hijo derecho es:"+ctx.getChild(2).getText());
                String value = ctx.getChild(2).getText(); // tomo el texto


                Token token = ctx.getStop(); // tomo la ultima parte del token (osea donde esta el texto a la derecha del like)
                //System.out.println("el token es : "+token);
                //System.out.println("el value es : "+value);
                this.rewriter.replace(token.getTokenIndex(), CleanLikeContent(value)); //metodo de Antlr que sirve para reemplazar el texto del token
                //System.out.println("el rewriter devolvio : "+token);
            }
        }
        return visitChildren(ctx);
    }
}
