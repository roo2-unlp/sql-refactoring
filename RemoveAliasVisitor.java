

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


      @Override
     public String visitParse(SQLiteParser.ParseContext ctx) {
         return ctx.getText() + " ";
     }

      @Override   
      public String  visitExpr(SQLiteParser.ExprContext ctx) {
         if (ctx.table_name() != null){ 
          if(ctx.table_name().any_name().IDENTIFIER().toString().equals(alias)) {
            return this.getAliasReference() + " ";
         }
       } 
       // verifica que el alias donde esta parado sea igual al input de alias dado y la idea es cambiarlo por el nombre de la tabla
        return ctx.getText() + " ";
        
      }
      @Override
      public String  visitTable_or_subquery(SQLiteParser.Table_or_subqueryContext ctx) {
         if (ctx.table_alias() != null){
           if(ctx.table_alias().any_name().IDENTIFIER().toString().equals(alias)) {
               return " ";
             // eliminar un nodo
         }
      }
         
         return ctx.getText() + " ";
       // este se para en el from y revisa si existe algun alias en esa columna
      }

     
    
       @Override 
       public String  visitJoin_constraint(SQLiteParser.Join_constraintContext ctx) {
         
         if(ctx.expr().table_name() != null){
           if(ctx.expr().table_name().any_name().IDENTIFIER().toString().equals(alias)){
               return this.getAliasReference() + " ";
               
         }
      }
        
            return ctx.getText() + " ";
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
            return this.getAliasReference() + " ";  
         }
      }
         else{
            if(ctx.column_alias() !=null) {
               if( ctx.column_alias().IDENTIFIER().toString().equals(alias)){
                  return " ";
               }
            } 
         }
      
         return ctx.getText() + " ";
         }
	
       
      @Override 
      public String visitOrdering_term(SQLiteParser.Ordering_termContext ctx) { 
         if(ctx.expr().table_name() != null){
          if (ctx.expr().table_name().any_name().IDENTIFIER().toString().equals(alias)){
                return this.getAliasReference() + " ";
         }
      }
         return ctx.getText() + " ";
      }
   //   public String getTextWithSpaces(ParserRuleContext ctx) {
   //      TokenStream tokens = ((Parser) getInterpreter()).getTokenStream();
   //      Interval interval = new Interval(ctx.getStart().getTokenIndex(), ctx.getStop().getTokenIndex());
   //      List<Token> tokenList = tokens.getTokens(interval.a, interval.b);

   //      StringBuilder sb = new StringBuilder();
   //      for (Token token : tokenList) {
   //          sb.append(token.getText()).append(" ");
   //      }
   //      return sb.toString().trim(); // Elimina el espacio adicional al final, si lo hubiera
   //  }
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
          return sb.toString();
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