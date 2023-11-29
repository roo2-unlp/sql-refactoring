import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import sqlitegrammar.*;

public class CheckPostconditionsVisitor extends SQLiteParserBaseVisitor<String> {

    // alias que se cambió
    private String alias;
    // viejo alias
    private String oldAlias;

    public CheckPostconditionsVisitor(String alias, String oldAlias) {
        super();
        this.alias = alias;
        this.oldAlias = oldAlias;
    }

    public String visitTable_alias(SQLiteParser.Table_aliasContext ctx) {

        String currentAlias = ctx.getText();

        // Verificar que el alias esté en la consulta
        if (currentAlias.equalsIgnoreCase(this.alias) && !currentAlias.equalsIgnoreCase(this.oldAlias))
            return ctx.getText();
        else
            System.err.println("Error: El nuevo alias ya existe en la consulta.");
        return null;

    }

    @Override
    public String visitColumn_alias(SQLiteParser.Column_aliasContext ctx) {

        String currentAlias = ctx.getText();

        // Verificar que el nuevo alias no se repita en la consulta
        if (!currentAlias.equalsIgnoreCase(oldAlias) && currentAlias.equalsIgnoreCase(alias)) {
            System.out.println(ctx.getText());
            return ctx.getText();
        } else
            System.err.println("Error: El nuevo alias ya existe en la consulta.");
        return null;
    }
}