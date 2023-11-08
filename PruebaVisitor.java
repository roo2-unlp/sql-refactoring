import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import sqlitegrammar.*;

public class PruebaVisitor extends SQLiteParserBaseVisitor<String> {
    @Override
    public String visitParse(SQLiteParser.ParseContext ctx) {
        String contexto = ctx.getText();
        System.out.println("Contexto: " + contexto);
        return contexto;
    }
}