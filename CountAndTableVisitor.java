import java.util.ArrayList;
import java.util.List;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import sqlitegrammar.*;


public class CountAndTableVisitor extends SQLiteParserBaseVisitor<Void> {

    List<ParserRuleContext> countNodes = new ArrayList<>(4);
    List<String> tableNames = new ArrayList<>(4);

    public List<ParserRuleContext> getCountNodes() {
        return this.countNodes;
    }
    public List<String> getTableNamesAndAlias() {
        return tableNames;
    }

    @Override
    public Void visitTable_or_subquery(SQLiteParser.Table_or_subqueryContext ctx) {
        // Verifica si el nodo actual es una tabla o subconsulta.
        if (ctx.table_name() != null) {
            // Obtiene el nombre de la tabla o alias.
            if (ctx.table_alias() != null){
                this.tableNames.add(ctx.table_alias().getText());
            }else{
                this.tableNames.add(ctx.table_name().getText());
            }
        }

        // Se pueden analizar más detalles de esta tabla o subconsulta si es necesario.
        return super.visitTable_or_subquery(ctx);
    }

    @Override
    public Void visitExpr(SQLiteParser.ExprContext ctx) {
        // Verifica si el nodo actual es una función de agregación "COUNT".
        if (ctx.function_name() != null && ctx.function_name().getText().equalsIgnoreCase("COUNT")) {
            // Verifica si el nodo siguiente es un paréntesis abierto "(".
            if (ctx.getChild(1) instanceof TerminalNode &&
                    ((TerminalNode) ctx.getChild(1)).getSymbol().getText().equals("(")) {
                // En este punto, estás dentro de la función COUNT. Puedes capturar el nodo completo.
                ParserRuleContext countFunctionNode = ctx;
                this.countNodes.add(ctx);
            }
        }
        return super.visitExpr(ctx);
    }

}
