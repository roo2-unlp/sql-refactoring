import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import org.antlr.v4.runtime.tree.ParseTree;

import sqlitegrammar.*;

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
        String currentAlias = ctx.IDENTIFIER().getText();
        if (currentAlias.equalsIgnoreCase(this.alias)) {
            // Renombrar el alias
            ctx.IDENTIFIER().setText(this.newAlias);
        }
        return super.visitColumn_alias(ctx);
    }

    @Override
    public String visitTable_alias(SQLiteParser.Table_aliasContext ctx) {
        String currentAlias = ctx.IDENTIFIER().getText();
        if (currentAlias.equalsIgnoreCase(this.alias)) {
            // Renombrar el alias
            ctx.IDENTIFIER().setText(this.newAlias);
        }
        return super.visitTable_alias(ctx);
    }
    
    @Override
    public String visitAlias(SQLiteParser.AliasContext ctx) {
        String currentAlias = ctx.IDENTIFIER(1).getText(); // Obtener el nuevo alias
        if (currentAlias.equalsIgnoreCase(this.alias)) {
            // Renombrar el alias
            ctx.IDENTIFIER(1).setText(newAlias);
        }
        return super.visitAlias(ctx);
    }
}