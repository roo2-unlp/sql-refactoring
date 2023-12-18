import org.antlr.v4.runtime.CommonToken;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.antlr.v4.runtime.tree.TerminalNodeImpl;

public class TransformAliasVisitor extends SQLiteParserBaseVisitor<String> {
    private String alias;
    private String newAlias;

    public TransformAliasVisitor(String alias, String newAlias) {
        super();
        this.alias = alias;
        this.newAlias = newAlias;
    }

    @Override
    public String visitColumn_alias(SQLiteParser.Column_aliasContext ctx) {
        // Obtener el nodo original
        TerminalNodeImpl originalNode = (TerminalNodeImpl) ctx.getChild(0);
        if (originalNode.getText().equals(this.alias)) {
            // Obtener el token original
            Token originalToken = originalNode.getSymbol();

            // Crear un nuevo token con el nuevo alias
            CommonToken newAliasToken = new CommonToken(originalToken);
            newAliasToken.setText(newAlias);

            // Crear un nuevo nodo terminal para el nuevo alias
            TerminalNodeImpl newAliasNode = new TerminalNodeImpl(newAliasToken);

            // Reemplazar el nodo original con el nuevo nodo
            ParserRuleContext parent = ctx.getParent();
            int index = parent.children.indexOf(originalNode);
            parent.children.set(2, newAliasNode);
        }

        return super.visitColumn_alias(ctx);
    }

    @Override
    public String visitTable_alias(SQLiteParser.Table_aliasContext ctx) {
        // Obtener el nodo original
        TerminalNodeImpl originalNode = (TerminalNodeImpl) ctx.any_name().getChild(0);

        if (originalNode.getText().equals(this.alias)) {
            // Obtener el token original
            Token originalToken = originalNode.getSymbol();

            // Crear un nuevo token con el nuevo alias
            CommonToken newAliasToken = new CommonToken(originalToken);
            newAliasToken.setText(newAlias);

            // Crear un nuevo nodo terminal para el nuevo alias
            TerminalNodeImpl newAliasNode = new TerminalNodeImpl(newAliasToken);

            // Reemplazar el nodo original con el nuevo nodo
            // ParserRuleContext parent = ctx.any_name().getParent();
            // int index = parent.children.indexOf(originalNode);
            ctx.any_name().children.set(0, newAliasNode);
        }

        return super.visitTable_alias(ctx);
    }

    @Override
    public String visitColumn_name(SQLiteParser.Column_nameContext ctx) {
        // Obtener el nodo original
        if (ctx.any_name() != null) {
            TerminalNodeImpl originalNode = (TerminalNodeImpl) ctx.any_name().getChild(0);

            if (originalNode.getText().equals(this.alias)) {
                // Obtener el token original
                Token originalToken = originalNode.getSymbol();

                // Crear un nuevo token con el nuevo alias
                CommonToken newAliasToken = new CommonToken(originalToken);
                newAliasToken.setText(newAlias);

                // Crear un nuevo nodo terminal para el nuevo alias
                TerminalNodeImpl newAliasNode = new TerminalNodeImpl(newAliasToken);

                // Reemplazar el nodo original con el nuevo nodo
                ParserRuleContext parent = ctx.any_name().getParent();
                int index = parent.children.indexOf(originalNode);
                parent.children.set(0, newAliasNode);
            }
        }
        return super.visitColumn_name(ctx);
    }

    @Override
    public String visitTable_name(SQLiteParser.Table_nameContext ctx) {
        if (ctx.any_name() != null) {
            // Obtener el nodo original
            TerminalNodeImpl originalNode = (TerminalNodeImpl) ctx.any_name().getChild(0);

            if (originalNode.getText().equals(this.alias)) {
                // Obtener el token original
                Token originalToken = originalNode.getSymbol();

                // Crear un nuevo token con el nuevo alias
                CommonToken newAliasToken = new CommonToken(originalToken);
                newAliasToken.setText(newAlias);

                // Crear un nuevo nodo terminal para el nuevo alias
                TerminalNodeImpl newAliasNode = new TerminalNodeImpl(newAliasToken);

                // Reemplazar el nodo original con el nuevo nodo
                ParserRuleContext parent = ctx.any_name().getParent();
                int index = parent.children.indexOf(originalNode);
                parent.children.set(0, newAliasNode);
            }
        }

        return super.visitTable_name(ctx);
    }

    @Override
    protected String defaultResult() {
        return "";
    }

    @Override
    protected String aggregateResult(String aggregate, String nextResult) {
        if (aggregate == null) {
            return nextResult; // Si el resultado acumulado es null, simplemente devuelve el resultado del nodo
                               // hijo
        } else {
            return aggregate + nextResult; // Concatena los resultados
        }
    }

    public String getAlias() {
        return this.alias;
    }

    public String getNewAlias() {
        return this.newAlias;
    }
}