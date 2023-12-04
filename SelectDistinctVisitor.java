import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import sqlitegrammar.*;

public class SelectDistinctVisitor extends SQLiteParserBaseVisitor<String> {

    @Override
    public String visitSelect_core(SQLiteParser.Select_coreContext ctx) {
        // Este método se llamará cuando el visitor se encuentre en un nodo 'Select_coreContext'
        if (ctx.getChild(1).getText().equalsIgnoreCase("DISTINCT")) {
            // Realiza acciones específicas para el nodo DISTINCT
            System.out.println("Visitando un nodo DISTINCT");
        }

        // Puedes realizar otras acciones específicas para el nodo 'Select_coreContext' en general

        return null;
    }
}
