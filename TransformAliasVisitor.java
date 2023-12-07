import org.antlr.v4.runtime.CommonToken;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.TerminalNodeImpl;

public class TransformAliasVisitor extends SQLiteParserBaseVisitor<String> {
    private String alias;
    private String newAlias;
    private StringBuilder consultaSQL;

    public TransformAliasVisitor(String alias, String newAlias) {
        super();
        this.alias = alias;
        this.newAlias = newAlias;
        this.consultaSQL = new StringBuilder();
    }

    @Override
    public String visitColumn_alias(SQLiteParser.Column_aliasContext ctx) {
        System.out.println("VISIT COLUMN ALIAS TRANSFORM");
        // Obtener el nodo original
        TerminalNodeImpl originalNode = (TerminalNodeImpl) ctx.getChild(0);
        // Obtener el token original
        Token originalToken = originalNode.getSymbol();

        // Crear un nuevo token con el nuevo alias
        CommonToken newAliasToken = new CommonToken(originalToken);
        newAliasToken.setText(newAlias);

        // Crear un nuevo nodo terminal para el nuevo alias
        TerminalNodeImpl newAliasNode = new TerminalNodeImpl(newAliasToken);

        // Reemplazar el nodo original con el nuevo nodo
        ParserRuleContext parent = ctx.getParent();
        //int index = parent.children.indexOf(originalNode);
        parent.children.set(2, newAliasNode);

        // Concatenar la parte transformada de la consulta SQL
        consultaSQL.append(ctx.getText());

        return ctx.getText();
    }

    @Override
    public String visitTable_alias(SQLiteParser.Table_aliasContext ctx) {
        System.err.println("VISIT TABLE ALIAS TRANSFORM");
        // Obtener el nodo original
        TerminalNodeImpl originalNode = (TerminalNodeImpl) ctx.any_name().getChild(0);

        if (originalNode.equals(this.alias)) {

            // Obtener el token original
            Token originalToken = originalNode.getSymbol();

            // Crear un nuevo token con el nuevo alias
            CommonToken newAliasToken = new CommonToken(originalToken);
            newAliasToken.setText(newAlias);

            // Crear un nuevo nodo terminal para el nuevo alias
            TerminalNodeImpl newAliasNode = new TerminalNodeImpl(newAliasToken);

            // Reemplazar el nodo original con el nuevo nodo
            ParserRuleContext parent = ctx.any_name().getParent();
            //int index = parent.children.indexOf(originalNode);
            parent.children.set(2, newAliasNode);
        }

        // Concatenar la parte transformada de la consulta SQL
        consultaSQL.append(ctx.getText());

        return ctx.getText();
    }

    // Método para obtener la consulta SQL completa
    public String obtenerConsultaSQL() {
        return consultaSQL.toString();
    }

    @Override
    protected String defaultResult() {
        return "";
    }

    @Override
    protected String aggregateResult(String aggregate, String nextResult) {
        if (aggregate == null) {
            return nextResult;  // Si el resultado acumulado es null, simplemente devuelve el resultado del nodo hijo
        } else {
            return aggregate + nextResult;  // Concatena los resultados
        }
    }
}
