import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import sqlitegrammar.*;

public class AliasVisitor extends SQLiteParserBaseVisitor<String> {
    @Override
    public String visitColumn_alias(SQLiteParser.Column_aliasContext ctx){
        
        ctx.column_alias().getText().equals(alias)
        return contexto;
    }

    public static void main(String[] args) {
        PruebaVisitor p = new PruebaVisitor();
        p.visitParse(SQLiteParser.ParseContext ctx);
    }
}