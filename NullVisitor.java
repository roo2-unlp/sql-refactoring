public class NullVisitor extends SQLiteParserBaseVisitor<String>{
    @Override
    public String visitParse(SQLiteParser.ParseContext ctx) {
        return ctx.getText();
    }
}
