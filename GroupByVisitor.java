import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import sqlitegrammar.*;

public class GroupByVisitor extends SQLiteParserBaseVisitor<String>{
    @Override
    public String visitSelect_core(SQLiteParser.Select_coreContext context) { 
        //TODO chequear si necesitamos este visitParse u otro metodo de SQLITEParserBaseVisitor
        return context.getText(); 
    }
}
