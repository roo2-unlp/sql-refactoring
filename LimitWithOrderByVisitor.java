import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import sqlitegrammar.*;

public class LimitWithOrderByVisitor extends SQLiteParserBaseVisitor<String>{
    @Override
    //public String visitParse(SQLiteParser.ParseContext ctx) { 
        //return ctx.getText(); 
    //}

    //return type Boolean is not compatible with String where T is a type-variable:
    //T extends Object declared in interface SQLiteParserVisitor LimitWithOrderByVisitor.java:6: error: method does not override or implement a method from a supertype
    public Boolean visitOrder_by_stmt(SQLiteParser.Order_by_stmtContext ctx){
        return true;
    }
}

