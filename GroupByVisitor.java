import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import sqlitegrammar.*;

public class GroupByVisitor extends SQLiteParserBaseVisitor<String>{
    @Override
    public String visitParse(SQLiteParser.ParseContext ctx) { 
        //TODO chequear si necesitamos este visitParse u otro metodo de SQLITEParserBaseVisitor
        return ctx.getText(); 
    }
    @Override
    public String visitSelect_core(SQLiteParser.Select_coreContext ctx){
        return "";

    }
}
