import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import sqlitegrammar.*;

public class NullVisitor extends SQLiteParserBaseVisitor<String>{
    @Override
    public String visitParse(SQLiteParser.ParseContext ctx) { 
        return ctx.getText(); 
    }
}
