import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import sqlitegrammar.*;

public class TransformAliasVisitor extends SQLiteParserBaseVisitor<String> {
    private String newAlias;

    public TransformAliasVisitor(String newAlias) {
        super();
        this.newAlias = newAlias;
    }

    public String visitAlias(SQLiteParser.Table_aliasContext ctx) {
        ctx = newAlias;
        return ctx;
    }
}
