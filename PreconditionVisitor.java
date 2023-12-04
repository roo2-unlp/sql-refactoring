import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import sqlitegrammar.*;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;
import java.util.*;

public class PreconditionVisitor extends SQLiteParserBaseVisitor<Boolean> {

    private boolean cumplePrecondicion;


    @Override
    public Boolean visitExpr(SQLiteParser.ExprContext exprContext) { 
        //Precondicon - Debe existir la clausula OR y no tiene que existir el and
        if ((exprContext.OR_() != null) && (exprContext.AND_() ==null) ){   
             
            this.cumplePrecondicion = true;         
            List<SQLiteParser.ExprContext> hijos = exprContext.expr();
            for (SQLiteParser.ExprContext hijo : hijos) {
                //Precondicon - La conversion solo se hara si se compara con igualdad         
                if (hijo.ASSIGN() == null){
                    this.cumplePrecondicion = false;
                }    
            }
        }
        System.out.println("mi visitExpr devuelve : " +  cumplePrecondicion);
        return cumplePrecondicion;
    }

    public boolean getCumplePrecondicion() {
        return this.cumplePrecondicion;
    }
}