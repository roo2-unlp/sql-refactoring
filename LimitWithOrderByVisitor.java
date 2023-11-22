import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import sqlitegrammar.*;
import org.antlr.v4.runtime.tree.ParseTree;

public class LimitWithOrderByVisitor extends SQLiteParserBaseVisitor<Void> {

    private StringBuilder transformedText = new StringBuilder();
    private boolean hasLimit = false;

    @Override
    public Void visitLimit_stmt(SQLiteParser.Limit_stmtContext ctx) {
        // La sentencia tiene el LIMIT
        hasLimit = true;
        return super.visitLimit_stmt(ctx);
    }

    @Override
    public Void visitOrder_by_stmt(SQLiteParser.Order_by_stmtContext ctx) {
        // La sentencia tiene el ORDER BY
        hasLimit = true;  // Si hay ORDER BY, también consideramos que tiene LIMIT
        return super.visitOrder_by_stmt(ctx);
    }

    @Override
    public Void visitOrdering_term(SQLiteParser.Ordering_termContext ctx) {
        // para modificar el texto como queramos
        transformedText.append(ctx.getText()).append(" ");
        return super.visitOrdering_term(ctx);
    }

    public boolean hasLimit() {
        // Devuelve true si la consulta tiene LIMIT
        return hasLimit;
    }

    public String getText() {
        // Devuelve el texto transformado
        return transformedText.toString().trim();
    }
}