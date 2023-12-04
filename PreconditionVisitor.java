import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import sqlitegrammar.*;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;
import java.util.*;

public class PreconditionVisitor extends SQLiteParserBaseVisitor<Boolean> {

    private boolean cumplePrecondicion;
    private boolean cumplePreconcionNombreCampo = true;

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
        return cumplePrecondicion;
    }

    @Override
    public Boolean visitSelect_core(SQLiteParser.Select_coreContext ctx) {
        String nombreCampo = ctx.whereExpr.expr().get(0).getChild(0).getText();
        for (int i = 1; i < ctx.whereExpr.expr().size(); i++){
            if ( !nombreCampo.equals(ctx.whereExpr.expr().get(i).getChild(0).getText())){
                this.cumplePreconcionNombreCampo= false;
            }
        }
        
        return super.visitSelect_core(ctx);
    }

    @Override
    protected Boolean defaultResult() {
        return cumplePrecondicion && cumplePreconcionNombreCampo;

    }

}