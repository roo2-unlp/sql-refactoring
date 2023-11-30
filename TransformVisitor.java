import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import sqlitegrammar.*;

import java.util.*;

public class TransformVisitor extends SQLiteParserBaseVisitor<String> {

    private StringBuilder contenidoSelect = new StringBuilder();
    private StringBuilder contenidoFrom = new StringBuilder();
    private StringBuilder contenidoWhere = new StringBuilder();
    private Set<String> columnasWhere = new HashSet<String>();

    //SELECT y su contenido
    @Override
    public String visitSelect_core(SQLiteParser.Select_coreContext ctx) {
        // String selectClause = ctx.SELECT_().getText(); // Obtiene la cláusula SELECT
        // System.out.println("Que imprime: " + selectClause);
        // for (SQLiteParser.Result_columnContext resultColumnContext : ctx.result_column()) {
        //     System.out.println("Campo del SELECT: " + resultColumnContext.getText());
        // }
        contenidoSelect.append("SELECT ");
        List<SQLiteParser.Result_columnContext> resultColumns = ctx.result_column();
        for (int i = 0; i < resultColumns.size(); i++) {
            contenidoSelect.append(resultColumns.get(i).getText());
            if (i < resultColumns.size() - 1) {
                contenidoSelect.append(",");
            }
        }
        contenidoSelect.append(" FROM ");
        return super.visitSelect_core(ctx);
    }


    //ESTA EL CONTENIDO DE WHERE
     @Override   
      public String  visitExpr(SQLiteParser.ExprContext ctx) {
        //System.out.println("--------VisitExpr--------");
        //System.out.println("ctx" + ctx.getText());
        if(ctx.column_name() != null){
          System.out.println("columnas " + ctx.column_name().getText());
          contenidoWhere.append(ctx.column_name().getText());
          contenidoWhere.append(" IN");
          contenidoWhere.append("(");
            //System.out.println("column_name" + ctx.column_name().getText());
        }
        if(ctx.literal_value() != null){
          if (contenidoWhere.length() > 0) {
                contenidoWhere.append(", ");
            }
          contenidoWhere.append(ctx.literal_value().getText());
            //System.out.println("literal_value" + ctx.literal_value().getText());
        }
        contenidoWhere.append(")");
        return super.visitExpr(ctx);
        
      }

      //TABLAS DEL FROM
      @Override
      public String  visitTable_or_subquery(SQLiteParser.Table_or_subqueryContext ctx) {
        //System.out.println("ctx.table_Table_or_subquery() " + ctx.table_name().getText());
        contenidoFrom.append(ctx.table_name().getText());
        return super.visitTable_or_subquery(ctx);
      }

    public String transformacion() {
        StringBuilder consulta = new StringBuilder();
        consulta.append(contenidoSelect).append(contenidoFrom);
        if (contenidoWhere.length() > 0) {
            consulta.append(" WHERE ").append(contenidoWhere);
        }
        System.out.println(consulta.toString());
        return consulta.toString();
    }



    public String getTransformacion() {
        return "SELECT * FROM empleados WHERE estado_civil IN ('Soltero', 'Casado')";
    }
}