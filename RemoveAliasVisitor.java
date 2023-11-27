
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.Interval;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

//import grammarSQLite.*;
// import sqlitegrammar.SQLiteParser;
// import grammarSQLite.SQLiteParser$Table_or_subqueryContext;
// import grammarSQLite.SQLiteParser$ExprContext;
// import grammarSQLite.SQLiteParser$Join_constraintContext;
// import grammarSQLite.SQLiteParser$Result_columnContext;
// import grammarSQLite.SQLiteParser$Ordering_termContext;
// // import sqlitegrammar.SQLiteParser;

import java.util.List;

public class RemoveAliasVisitor extends SQLiteParserBaseVisitor<String>{
      private String alias=null;
      private String aliasReference=null;
      private StringBuilder queryBuilder = new StringBuilder();
      private int resultCounter, columnSize = 0;
      private boolean selectVisited = false;
      private boolean fromVisited = false;
      // @Override
      // public String visitParse(SQLiteParser.ParseContext ctx) {
      //    queryBuilder.append(ctx.getText() + " ");
      //     return ctx.getText() + " ";
      // }
      
      @Override
      public String   visitSelect_core(SQLiteParser.Select_coreContext ctx) {
         if (!selectVisited) {
            if (ctx.SELECT_() != null) {
                queryBuilder.append(ctx.SELECT_().toString()).append(" ");
                selectVisited = true;
            }
        }

        if (!ctx.result_column().isEmpty()) {
            int columnSize = ctx.result_column().size();
          

            for (SQLiteParser.Result_columnContext columnContext : ctx.result_column()) {
                resultCounter++;
                queryBuilder.append(columnContext.getText());

                if (resultCounter < columnSize) {
                    queryBuilder.append(", ");
                } else if (!fromVisited) {
                    queryBuilder.append(" FROM ");
                    fromVisited = true;
                }
            }
        }

        // Visitando los nodos WHERE, GROUP BY y ORDER BY
      //   if (ctx.whereExpr != null) {
      //       queryBuilder.append(" WHERE ");
      //       super.visit(ctx.whereExpr);
      //   }

      //   if (ctx.groupByExpr != null) {
      //       if (selectVisited && !fromVisited) {
      //           queryBuilder.append(" FROM "); // Añadir "FROM" si no se ha agregado antes
      //           fromVisited = true;
      //       }
      //       queryBuilder.append(" GROUP BY ");
      //       super.visit(ctx.groupByExpr);
      //   }

      //   if (ctx.order_by_stmt.ORDER_()!= null) {
      //       queryBuilder.append(" ORDER BY ");
      //       super.visit(ctx.order_by_stmt.Ordering_termContext);
      //   }

        return null;
    }
      @Override   
      public String  visitExpr(SQLiteParser.ExprContext ctx) {
         System.out.println("paso 2");
         if (ctx.table_name() != null){ 
          if(ctx.table_name().any_name().IDENTIFIER().toString().equals(alias)) {
            queryBuilder.append(this.getAliasReference() + " ");
            //return this.getAliasReference() + " ";
         }
       } 
       // verifica que el alias donde esta parado sea igual al input de alias dado y la idea es cambiarlo por el nombre de la tabla
        return ctx.getText().trim();
      }
      @Override
      public String  visitTable_or_subquery(SQLiteParser.Table_or_subqueryContext ctx) {
         if (ctx.table_alias() != null){
           if(ctx.table_alias().any_name().IDENTIFIER().toString().equals(alias)) {
               System.out.println("sssssssssssssssssssssssss");
               queryBuilder.append(" ");
               //return " ";
             // eliminar un nodo
         }
         System.out.println("aaaaaaaaaaaa");
      }
         System.out.println("paso 3");
         return ctx.getText().trim();
       // este se para en el from y revisa si existe algun alias en esa columna
      }

     
    
       @Override 
       public String  visitJoin_constraint(SQLiteParser.Join_constraintContext ctx) {
         
         if(ctx.expr().table_name() != null){
           if(ctx.expr().table_name().any_name().IDENTIFIER().toString().equals(alias)){
               System.out.println("paso 4");
                queryBuilder.append(this.getAliasReference() + " ");
               //return this.getAliasReference() + " ";
               
         }
      }
         System.out.println("paso 5");
            return ctx.getText().trim();
       }
      // @Override 
      // public String  visitJoin_clause(SQLiteParser.Join_clauseContext ctx) { 
      //       if(ctx.table_or_subquery() != null){
      //          ctx.table_or_subquery().table_alias().getText().remove();
      //       }
            
      //    } CREO QUE NO HACE FALTA ESTA

      @Override 
      public String visitResult_column(SQLiteParser.Result_columnContext ctx) {
        
          if(ctx.table_name() != null){
          if(ctx.table_name().any_name().IDENTIFIER().toString().equals(alias)){
            System.out.println("paso 6");
            queryBuilder.append(this.getAliasReference() + " ");
            //return this.getAliasReference() + " ";  
         } 
      }
         else{
            if(ctx.column_alias() !=null) {
               if( ctx.column_alias().IDENTIFIER().toString().equals(alias)){
                  queryBuilder.append(" ");
                  //return " ";
               }
            } 
         }
      
         return ctx.getText().trim();
         }
	
      
      @Override 
      public String visitOrdering_term(SQLiteParser.Ordering_termContext ctx) { 
         if(ctx.expr().table_name() != null){
          if (ctx.expr().table_name().any_name().IDENTIFIER().toString().equals(alias)){
              queryBuilder.append(this.getAliasReference() + " ");
              //return this.getAliasReference() + " ";
         }
      }
         return ctx.getText().trim();
      }
    
   
      //  public String getTextWithSpaces(SQLiteParser.ParseContext ctx) {
      //     TokenStream tokens = ((Parser)getInterpreter()).getTokenStream();
      //     Interval interval = new Interval(ctx.getStart().getTokenIndex(), ctx.getStop().getTokenIndex());
      //     List<Token> tokenList = tokens.getTokens(interval.a, interval.b);
      //     StringBuilder sb = new StringBuilder();
      //     for (Token token : tokenList) {
      //         sb.append(token.getText()).append(" ");
      //     }
      //     return sb.toString().trim(); // Elimina el espacio adicional al final, si lo hubiera
      // }
        @Override
        public String aggregateResult(String aggregate, String nextResult) {
           if (aggregate == null) {
              return nextResult;
           }
           if (nextResult == null) {
              return aggregate;
           }
           StringBuilder sb = new StringBuilder(aggregate);
           sb.append(" ");
           sb.append(nextResult);
           System.out.println(sb.toString());
           return sb.toString();
        }
        @Override
        public String defaultResult() {
            return this.queryBuilder.toString();
        }
      public void setAlias(String alias){
         this.alias=alias;
      }
      public String getAlias(String alias){
         return alias;
      }
      public void setAliasReference(String aliasReference){
         this.aliasReference=aliasReference;
      } 
      public String getAliasReference(){
         return aliasReference;
      }
      }