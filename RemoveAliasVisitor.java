
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
      private boolean whereIs,groupIs,fromIs,havingIs,selectIs = false;

         // @Override
         // public String visitParse(SQLiteParser.ParseContext ctx){
         //    super.visitParse(ctx);
         //    return sourceTextForContext(ctx);
         // }

      //  @Override 
      //  public String visitSelect_core(SQLiteParser.Select_coreContext ctx) {
      //     if (ctx.SELECT_() != null ){
      //        selectIs=true;
      //     }
      //     if(ctx.WHERE_() != null){
      //        queryBuilder.append(ctx.WHERE_().toString() + " ");
      //        whereIs=true;
      //     }
      //     if(ctx.FROM_() != null){
      //        queryBuilder.append(ctx.FROM_().toString() + " ");
      //        fromIs=true;
      //     }
      //     if(ctx.GROUP_() != null){
      //        queryBuilder.append(ctx.GROUP_().toString() + " ");
      //        groupIs=true;
      //     }
      //     if(ctx.HAVING_() != null){
      //        queryBuilder.append(ctx.HAVING_().toString() + " ");
      //        havingIs= true;
      //     }
      //     return  super.visitSelect_core(ctx);
      //  }  
     public String sourceTextForContext(ParseTree context) {    
       Token startToken, stopToken;
       int stopIndex;
       if (context instanceof ParserRuleContext) {
           ParserRuleContext ctx = (ParserRuleContext) context;
           startToken = ctx.getStart();
           stopToken = ctx.getStop();
       } else {
           startToken = ((TerminalNode) context).getSymbol();
           stopToken = startToken;
         }
       if (startToken != null) {
           CharStream cs = startToken.getInputStream();
           int startIndex = startToken.getStartIndex();
           stopIndex = (stopToken != null) ? stopToken.getStopIndex() : -1;
           return cs.getText(new Interval(startIndex, stopIndex));
       
         }
         return "";
      }
      
       @Override   
       public String  visitExpr(SQLiteParser.ExprContext ctx) {
          if (ctx.expr(0).table_name() != null){ 
           if(ctx.expr(0).table_name().any_name().IDENTIFIER().toString().equals(alias)) {
             //queryBuilder.append(this.getAliasReference() + ctx.getText());
             System.out.println(sourceTextForContext(ctx).replace(alias,this.getAliasReference()));   
             return " WHERE " + sourceTextForContext(ctx).replace(ctx.expr(0).table_name().any_name().IDENTIFIER().toString(),this.getAliasReference());
          }
        } 
        
        // verifica que el alias donde esta parado sea igual al input de alias dado y la idea es cambiarlo por el nombre de la tabla
         return " WHERE " + sourceTextForContext(ctx);
       }
       
       @Override
       public String  visitTable_or_subquery(SQLiteParser.Table_or_subqueryContext ctx) {
          if (ctx.table_alias() != null){
            if(ctx.table_alias().any_name().IDENTIFIER().toString().equals(alias)) {
               String ctxAs = sourceTextForContext(ctx).replace(ctx.AS_().toString(), "");
               return  " FROM " +  ctxAs.replace(ctx.table_alias().any_name().IDENTIFIER().toString(), "");
                
                // eliminar un nodo
          }       
       }       
          return " FROM " + sourceTextForContext(ctx);
        // este se para en el from y revisa si existe algun alias en esa columna
       }

     
    
      //   @Override 
      //   public String  visitJoin_constraint(SQLiteParser.Join_constraintContext ctx) {       
      //     if(ctx.expr().expr(1).table_name() != null){
      //       if(ctx.expr().expr(1).table_name().any_name().IDENTIFIER().toString().equals(alias)){               
      //           return sourceTextForContext(ctx).replace(alias,this.getAliasReference());
                             
      //     }
      //  }      
      //        return sourceTextForContext(ctx);
      //   }
//       // @Override 
//       // public String  visitJoin_clause(SQLiteParser.Join_clauseContext ctx) { 
//       //       if(ctx.table_or_subquery() != null){
//       //          ctx.table_or_subquery().table_alias().getText().remove();
//       //       }
            
//       //    } CREO QUE NO HACE FALTA ESTA

       @Override 
       public String visitResult_column(SQLiteParser.Result_columnContext ctx) { 
         System.out.println(sourceTextForContext(ctx));     
           if(ctx.table_name() != null){
           if(ctx.table_name().any_name().IDENTIFIER().toString().equals(alias)){
               return " SELECT " +sourceTextForContext(ctx).replace(ctx.table_name().any_name().IDENTIFIER().toString(),this.getAliasReference());
               
          } 
       }
          else{
             if(ctx.column_alias() !=null) {
                if( ctx.column_alias().IDENTIFIER().toString().equals(alias)){
                  System.out.println(ctx.column_alias().IDENTIFIER().toString());
                   String ctxAs = sourceTextForContext(ctx).replace(ctx.AS_().toString(),"");
                   return "," + ctxAs.replace(ctx.column_alias().IDENTIFIER().toString(),"") ;
                }
             } 
          }      
          return " SELECT " + sourceTextForContext(ctx);
          }
      
      //   @Override 
      //   public String visitOrdering_term(SQLiteParser.Ordering_termContext ctx) { 
      //      if(ctx.expr().table_name() != null){
      //       if (ctx.expr().table_name().any_name().IDENTIFIER().toString().equals(alias)){
      //           //queryBuilder.append(" ORDER BY " + this.getAliasReference() + ctx.getText());
      //           return " ORDER BY " + sourceTextForContext(ctx).replace(ctx.expr().table_name().any_name().IDENTIFIER().toString(),this.getAliasReference());
      //      }
      //   }
      //      return " ORDER BY " + sourceTextForContext(ctx);
      //   }
    
   
//       //  public String getTextWithSpaces(SQLiteParser.ParseContext ctx) {
//       //     TokenStream tokens = ((Parser)getInterpreter()).getTokenStream();
//       //     Interval interval = new Interval(ctx.getStart().getTokenIndex(), ctx.getStop().getTokenIndex());
//       //     List<Token> tokenList = tokens.getTokens(interval.a, interval.b);
//       //     StringBuilder sb = new StringBuilder();
//       //     for (Token token : tokenList) {
//       //         sb.append(token.getText()).append(" ");
//       //     }
//       //     return sb.toString().trim(); // Elimina el espacio adicional al final, si lo hubiera
//       // }
         @Override
         public String aggregateResult(String aggregate, String nextResult) {
            if (aggregate == null) {
               return nextResult;
            }
            if (nextResult == null) {
               return aggregate;
            }
            StringBuilder sb = new StringBuilder(aggregate);
            sb.append("");
            sb.append(nextResult);         
            return sb.toString();
         }
//       //   @Override
//       //   public String defaultResult() {
//       //       return this.queryBuilder.toString();
//       //   }
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