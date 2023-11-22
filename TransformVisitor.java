import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import sqlitegrammar.*;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;
import java.util.*;


public class TransformVisitor extends SQLiteParserBaseVisitor<String>{
    private String transformacion;
    // public String visitParse(SQLiteParser.ParseContext ctx) { 
    //     return ctx.getText(); 
    // }
    // @Override
    // public String visitExpr(SQLiteParser.ExprContext ctx) {
    //     //System.out.println("ctx.getChildCount() " + ctx.getChildCount());
    //     //System.out.println("ctx.getChild(1).getText().equals(OR) " + ctx.getChild(1).getText().equals("OR"));
    //     if (ctx.getChildCount() >= 3 && ctx.getChild(1).getText().equals("OR")) {
    //         // Identify OR expression
    //         String fieldName = ctx.getChild(0).getText();
            
    //         List<String> values = new ArrayList<>();

    //         for (int i = 2; i < ctx.getChildCount(); i++) {
    //             values.add(ctx.getChild(i).getText());
    //         }

    //         // Construct IN expression
    //         String inExpression = fieldName + " IN (" + String.join(", ", values) + ")";
    //         this.transformacion = inExpression;
    //         // Replace OR expression with IN expression
    //         return inExpression;
    //     } else {
    //         // Recursively visit child nodes
    //         return super.visitExpr(ctx);
    //     }
    // }

//   public class TransformVisitor extends SQLiteParserBaseVisitor<String> {
//     @Override
//     public String visitParse(SQLiteParser.ParseContext ctx) {
//         String whereClause = "SELECT * FROM empleados WHERE ";
//         String inExpression = visitSql_stmt_list(ctx.sql_stmt_list(0)); // Suponiendo una única consulta SQL

//         // Combinar la parte fija de la consulta con la transformación del WHERE
//         return whereClause + inExpression;
//     }

//     @Override
//     public String visitSql_stmt_list(SQLiteParser.Sql_stmt_listContext ctx) {
//         return visitChildren(ctx);
//     }

//     @Override
//     public String visitExpr(SQLiteParser.ExprContext ctx) {
//         if (ctx.getChildCount() >= 3 && ctx.getChild(1).getText().equals("OR")) {
//             // Identificar la expresión OR
//             String fieldName = ctx.getChild(0).getText();

//             List<String> values = new ArrayList<>();
//             for (int i = 2; i < ctx.getChildCount(); i++) {
//                 values.add(ctx.getChild(i).getText());
//             }

//             // Construir la expresión IN
//             String inExpression = fieldName + " IN ('" + String.join("', '", values) + "')";
//             this.transformacion = inExpression;
//             // Devolver la expresión IN
//             return inExpression;
//         } else {
//             // Visitar nodos hijos recursivamente
//             return super.visitExpr(ctx);
//         }
//     }

 @Override
    public String visitSelect_core(SQLiteParser.Select_coreContext ctx) {
        String selectClause = ctx.SELECT_().getText(); // Obtiene la cláusula SELECT
        //String fromClause = ctx.FROM_().getText(); // Obtiene la cláusula FROM
        //System.out.println(" selectClause " +  ctx.result_column().get(0).getText() ); // esto para obtener los campos del select falta iterar la
        for (SQLiteParser.Result_columnContext resultColumnContext : ctx.result_column()){
            System.out.println("Campo del SELECT: " + resultColumnContext.getText());
            // Aquí puedes trabajar con cada campo del SELECT según necesites
        }
        //System.out.println(" fromClause " +  fromClause );


        return "String";
    }

        // // Agregar la parte FROM (suponiendo que table_name es un contexto)
        // SQLiteParser.Table_nameContext tableNameContext = ctx.getRuleContext(SQLiteParser.Table_nameContext.class, 0);
        // queryBuilder.append(" FROM ");
        // queryBuilder.append(tableNameContext.getText());

        // // Agregar la parte WHERE si existe
        // SQLiteParser.ExprContext exprContext = ctx.getRuleContext(SQLiteParser.ExprContext.class, 0);
        // if (exprContext != null) {
        //     queryBuilder.append(" WHERE ");
        //     queryBuilder.append(exprContext.getText());
        // }


    public String getTransformacion(){
        return this.transformacion;
    }
}