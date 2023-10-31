import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import sqlitegrammar.*;

public class LikeVisitor extends SQLiteParserBaseVisitor<String> {

    public String generalVisit(SQLiteParser.ParseContext ctx) {
        if (ctx.getChild(1).getText().equals("LIKE") || ctx.getChild(1).getText().equals("like")) {
            visitParse(ctx.getChild(2));
        }
        return visitChildren(ctx);
    }
    // como funciona el visitChildren? (lo vimos de un compañero)

    // como recorremos el arbol?

    // como evaluamos q estemos en el nodo correcto?

    // la regla es el literal_value ?

    @Override
    public String visitParse(SQLiteParser.ParseContext ctx) {
        String value = ctx.getText(); // tomo el texto
        String refactoredValue = value.replace("%", ""); // le saco todos los %
        return refactoredValue.concat("%"); // le agrego un unico % al final y se lo devuelvo
    }
}
