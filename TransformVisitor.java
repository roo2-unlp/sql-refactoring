import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import sqlitegrammar.*;

import java.util.*;

public class TransformVisitor extends SQLiteParserBaseVisitor<String> {

    private StringBuilder contenidoSelect = new StringBuilder();
    private StringBuilder contenidoFrom = new StringBuilder();
    private List<String> literalValues = new ArrayList<String>();
    private String campoWhere; 

    //SELECT y NOMBRE COLUMNA USADA EN EL WHERE
    @Override
    public String visitSelect_core(SQLiteParser.Select_coreContext ctx) {
        this.campoWhere = ctx.whereExpr.expr().get(1).getChild(0).getText();
        contenidoSelect.append("SELECT ");
        List<SQLiteParser.Result_columnContext> resultColumns = ctx.result_column();
        
        for (int i = 0; i < resultColumns.size(); i++) {
            contenidoSelect.append(resultColumns.get(i).getText());
            if (i < resultColumns.size() - 1) {
                contenidoSelect.append(",");
            }
        }

        return super.visitSelect_core(ctx);
    }

    //ESTA EL CONTENIDO DE LOS LITERAL VALUE DEL WHERE
     @Override   
      public String  visitExpr(SQLiteParser.ExprContext ctx) {
        if(ctx.literal_value() != null){
            this.literalValues.add(ctx.literal_value().getText());
        }
        return super.visitExpr(ctx);
        
      }

      //TABLAS DEL FROM
      @Override
      public String  visitTable_or_subquery(SQLiteParser.Table_or_subqueryContext ctx) {
        contenidoFrom.append(" FROM ");
        contenidoFrom.append(ctx.table_name().getText());
        return super.visitTable_or_subquery(ctx);
      }

    public String transformacion() {
        StringBuilder consulta = new StringBuilder();
        consulta.append(contenidoSelect).append(contenidoFrom);
        consulta.append(" WHERE ")
                .append(this.campoWhere)
                .append(" IN")
                .append("(");
        int contador = this.literalValues.size();
        for (int i = 0; i < this.literalValues.size(); i++){
            consulta.append(this.literalValues.get(i));
            if( i < contador-1){
                consulta.append(",");
            }
        }
        consulta.append(")");
        return consulta.toString();
    }

}