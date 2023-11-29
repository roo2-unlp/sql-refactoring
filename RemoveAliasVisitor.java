import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.Interval;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.List;

public class RemoveAliasVisitor extends SQLiteParserBaseVisitor<String>{
      private String alias=null;
      private String aliasReference=null;
      private StringBuilder queryBuilder = new StringBuilder();

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

      @Override
      public String aggregateResult(String aggregate, String nextResult) {
         if (aggregate == null) {
            return nextResult;
         }
         if (nextResult == null) {
            return aggregate;
         }
         StringBuilder sb = new StringBuilder(aggregate);
         sb.append(nextResult);         
         return sb.toString();
      }
  
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
         String izq = "";
         String der = "";
         if(ctx.expr(0) != null || ctx.expr(1) != null){
         if (ctx.expr(0).table_name() != null){ 
            if(ctx.expr(0).table_name().any_name().IDENTIFIER().toString().equals(alias)) {
               System.out.println(sourceTextForContext(ctx).replace(alias,this.getAliasReference()));   
               izq = sourceTextForContext(ctx).replace(ctx.expr(0).table_name().any_name().IDENTIFIER().toString(),this.getAliasReference());
            }
         }
         if (ctx.expr(1).table_name() != null){ 
            if(ctx.expr(1).table_name().any_name().IDENTIFIER().toString().equals(alias)) {
               System.out.println(sourceTextForContext(ctx).replace(alias,this.getAliasReference()));   
               der =  sourceTextForContext(ctx).replace(ctx.expr(1).table_name().any_name().IDENTIFIER().toString(),this.getAliasReference());
            }
         }
      }
      else{
         if(ctx.table_name() != null){
            if(ctx.table_name().any_name().IDENTIFIER().toString().equals(alias)){
               return " GROUP BY " +sourceTextForContext(ctx).replace(ctx.table_name().any_name().IDENTIFIER().toString(),this.getAliasReference());
            }
         }
      }
         return " WHERE " + izq + der;
       }
       
       @Override
       public String  visitTable_or_subquery(SQLiteParser.Table_or_subqueryContext ctx) {
         if (ctx.table_alias() != null){
            if(ctx.table_alias().any_name().IDENTIFIER().toString().equals(alias)) {
               String ctxAs = sourceTextForContext(ctx).replace(ctx.AS_().toString(), "");
               return  " FROM " +  ctxAs.replace(ctx.table_alias().any_name().IDENTIFIER().toString(), "");
            }       
         }
         System.out.println("SSSSSSSSSSSSSSSSSSSS");
         System.out.println(ctx.join_clause());
         if(ctx.join_clause() != null){
            return " JOIN " +sourceTextForContext(ctx) + " ON ";
         }
         

         return " FROM " + sourceTextForContext(ctx);
       }

      @Override 
         public String visitResult_column(SQLiteParser.Result_columnContext ctx) { 
               
            if(ctx.table_name() != null){
               System.out.println("WACHOOOOOOOOOOO");
               System.out.println(ctx.table_name().any_name().IDENTIFIER().toString());
            if(ctx.table_name().any_name().IDENTIFIER().toString().equals(alias)){
                  System.out.println("aaaaaaaaaaaaaaaaaaaaaaaa");
                  System.out.print(ctx.table_name().any_name().IDENTIFIER().toString());
                  return sourceTextForContext(ctx).replace(ctx.table_name().any_name().IDENTIFIER().toString(),this.getAliasReference());
                  
            } 
         }
            else{
               if(ctx.column_alias() !=null) {
                  if( ctx.column_alias().IDENTIFIER().toString().equals(alias)){
                     System.out.println(ctx.column_alias().IDENTIFIER().toString());
                     String ctxAs = sourceTextForContext(ctx).replace(ctx.AS_().toString(),"").trim();
                     return "," + ctxAs.replace(ctx.column_alias().IDENTIFIER().toString(),"").trim() ;
                  }
               } 
            } 
            System.out.println("paco cocaine");
            System.out.println(" SELECT " + sourceTextForContext(ctx)); 
         return " SELECT " + sourceTextForContext(ctx);
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
//  @Override 
//  public String  visitJoin_clause(SQLiteParser.Join_clauseContext ctx) { 
      
//       if(ctx.table_or_subquery(0) != null){
//          System.out.println("sssssssssssssssssssss");
//          System.out.println(ctx.table_or_subquery(0).table_alias().any_name().IDENTIFIER().toString());
//           if(ctx.table_or_subquery(0).table_alias().any_name().IDENTIFIER().toString().equals(alias)){
//                System.out.println("no pasa");
//                String ctxJoin= sourceTextForContext(ctx).replace(ctx.table_or_subquery(0).AS_().toString(),"").trim();
//                return ctxJoin.replace(ctx.table_or_subquery(0).table_alias().any_name().IDENTIFIER().toString(),"").trim();
//           }
//        }
//        if(ctx.table_or_subquery(1) != null){
//          System.out.println("aaaaaaaaaaaaaaaaaaaaaaaa");
//          System.out.println(ctx.table_or_subquery(1).table_alias().any_name().IDENTIFIER().toString());
//           if(ctx.table_or_subquery(1).table_alias().any_name().IDENTIFIER().toString().equals(alias)){
//                System.out.println(sourceTextForContext(ctx).replace(ctx.table_or_subquery(1).table_alias().any_name().IDENTIFIER().toString(),""));
//                String ctxJoin= sourceTextForContext(ctx).replace(ctx.table_or_subquery(1).AS_().toString(),"").trim();
//                return ctxJoin.replace(ctx.table_or_subquery(1).table_alias().any_name().IDENTIFIER().toString(),"").trim();
//           }
//        }   
//        if(ctx.join_constraint(0) != null){
//          if(ctx.join_constraint(0).expr().table_name().any_name().IDENTIFIER().toString().equals(alias)){
//             return sourceTextForContext(ctx).replace(ctx.join_constraint(0).expr().table_name().any_name().IDENTIFIER().toString(),this.getAliasReference());
//          }
//        }
//        if(ctx.join_constraint(1) != null){
//          if(ctx.join_constraint(1).expr().table_name().any_name().IDENTIFIER().toString().equals(alias)){
//             return sourceTextForContext(ctx).replace(ctx.join_constraint(1).expr().table_name().any_name().IDENTIFIER().toString(),this.getAliasReference());
//          }
//        }
//        return sourceTextForContext(ctx);
//       }
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
//         public String getTextWithSpaces(SQLiteParser.ParseContext ctx) {
//       //     TokenStream tokens = ((Parser)getInterpreter()).getTokenStream();
//       //     Interval interval = new Interval(ctx.getStart().getTokenIndex(), ctx.getStop().getTokenIndex());
//       //     List<Token> tokenList = tokens.getTokens(interval.a, interval.b);
//       //     StringBuilder sb = new StringBuilder();
//       //     for (Token token : tokenList) {
//       //         sb.append(token.getText()).append(" ");
//       //     }
//       //     return sb.toString().trim(); // Elimina el espacio adicional al final, si lo hubiera
//       // }
//       //   @Override
//       //   public String defaultResult() {
//       //       return this.queryBuilder.toString();
//       //   }
}