import org.antlr.v4.runtime.CommonToken;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.antlr.v4.runtime.tree.TerminalNodeImpl;

public class TransformAliasVisitor extends SQLiteParserBaseVisitor<String> {
    private String alias;
    private String newAlias;
    private StringBuilder separatedWords;
    private boolean esEspecial = false;
    private String column_name;
    private String table_name;

    public TransformAliasVisitor(String alias, String newAlias) {
        super();
        this.alias = alias;
        this.newAlias = newAlias;
        this.separatedWords = new StringBuilder();
    }

    @Override
    public String visitColumn_alias(SQLiteParser.Column_aliasContext ctx) {
        //System.out.println("VISIT COLUMN ALIAS TRANSFORM");
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
            this.column_name = parent.children.get(0).getText();
            parent.children.set(2, newAliasNode);
        }

        return super.visitColumn_alias(ctx);
    }

    @Override
    public String visitTable_alias(SQLiteParser.Table_aliasContext ctx) {
        // System.out.println("VISIT TABLE ALIAS TRANSFORM");
        // Obtener el nodo original
        TerminalNodeImpl originalNode = (TerminalNodeImpl) ctx.any_name().getChild(0);

        if (originalNode.getText().equals(this.alias)) {
            // System.out.println("entró al if del transform column alias");
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
        // System.out.println("VISIT COLUMN NAME TRANSFORM");
        // Obtener el nodo original
        if (ctx.any_name() != null) {
            TerminalNodeImpl originalNode = (TerminalNodeImpl) ctx.any_name().getChild(0);

            if (originalNode.getText().equals(this.alias)) {
                System.out.println("Entró al if column name " + originalNode.getText() + " nuevo " + this.newAlias);
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
        // System.out.println("VISIT TABLE NAME TRANSFORM");
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

    public String getSeparatedWords() {
        return separatedWords.toString();
    }

    @Override
    public String visitTerminal(TerminalNode node) {
        String text = node.getText();
        if (!text.equals("<EOF>"))
            appendSeparatedWords(text);

        return super.visitTerminal(node);
    }

    private void appendSeparatedWords(String text) {
        if (!text.equals(",") && !text.equals(";")) {
            if (separatedWords.length() > 0 && !text.equals(".")) {
                if (!this.esEspecial) {
                    separatedWords.append(" ");
                } else {
                    this.esEspecial = false;
                }
                separatedWords.append(text);
            } else {
                if (text.equals(".")) {
                    this.esEspecial = true;
                    separatedWords.append(text);
                } else {
                    separatedWords.append(text);
                    separatedWords.append(" ");
                }
            }
        } else
            separatedWords.append(text);
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