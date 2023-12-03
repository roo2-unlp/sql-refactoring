import org.antlr.v4.runtime.CommonToken;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
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
        parent.children.set(index, newAliasNode);

        return super.visitColumn_alias(ctx);

    }

    @Override
    public String visitTable_alias(SQLiteParser.Table_aliasContext ctx) {
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
	        int index = parent.children.indexOf(originalNode);
	        parent.children.set(index, newAliasNode);

        }

        return super.visitTable_alias(ctx);
    }

    @Override
    public String visitAlias(SQLiteParser.AliasContext ctx) {
        /*String currentAlias = ctx.IDENTIFIER(1).getText(); // Obtener el nuevo alias
        if (currentAlias.equalsIgnoreCase(this.alias)) {
            // Renombrar el alias
            ctx.IDENTIFIER(1).setText(newAlias);
        } */
        return super.visitAlias(ctx);
    }
}
