import sqlitegrammar.SQLiteParser;
import sqlitegrammar.SQLiteParserBaseVisitor;

public class CountVisitor extends SQLiteParserBaseVisitor<String> {
    @Override
    public String visitParse(SQLiteParser.ParseContext ctx) {
        return ctx.getText(); 
    }
}
