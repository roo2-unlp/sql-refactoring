import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.Interval;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.antlr.v4.runtime.tree.TerminalNodeImpl;
import org.antlr.v4.runtime.tree.RuleNode;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RemoveAliasVisitor extends SQLiteParserBaseVisitor<String>{
      private String alias=" ";
      private String aliasReference="";
      private StringBuilder querySeparate=new StringBuilder();
      private final Set<String> specialCharacters = new HashSet<>(Arrays.asList(".", "(", ")", ",", "="));

      public void setAlias(String alias){
         this.alias=alias;
      }
      public String getAlias(){
         return alias;
      }
      public void setAliasReference(String aliasReference){
         this.aliasReference=aliasReference;
      } 
      public String getAliasReference(){
         return aliasReference;
      }


      public String getQuerySeparete() {
         return querySeparate.toString();
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
           sb.append(" ");
           sb.append(nextResult);         
           return sb.toString();
        }
       
   
         //  public String sourceTextForContext(ParseTree context) {    
         //     Token startToken, stopToken;
         //     int stopIndex;     
         //      if (context instanceof ParserRuleContext) {
         //         ParserRuleContext ctx = (ParserRuleContext) context;
         //         startToken = ctx.getStart();
         //         stopToken = ctx.getStop();
         //         } else {
         //         startToken = ((TerminalNode) context).getSymbol();
         //         stopToken = startToken;
         //         }
         //      if (startToken != null) {
         //         CharStream cs = startToken.getInputStream();
         //         int startIndex = startToken.getStartIndex();
         //         stopIndex = (stopToken != null) ? stopToken.getStopIndex() : -1;
         //         return cs.getText(new Interval(startIndex, stopIndex));
         //      }
         //      return "";
         //  }
      
         
   
            @Override
            public String  visitTable_or_subquery(SQLiteParser.Table_or_subqueryContext ctx) {
             if(ctx.table_alias() != null){ 
              if(ctx.table_alias().any_name().IDENTIFIER().toString().equals(alias)) {
                 //System.out.println("TABLE");
                 visit(ctx.getChild(0));
                 return " ";       
               }
              
               }
               return visitChildren(ctx);
              }  
              
            
             
          @Override 
           public String visitResult_column(SQLiteParser.Result_columnContext ctx) {
              if(ctx.column_alias() != null){
              if(ctx.column_alias().IDENTIFIER().toString().equals(alias)){
                 //System.out.println("COLUMN");
                 visit(ctx.getChild(0));
                 return " ";   
                 }
              }
              return visitChildren(ctx);
             }

             
              @Override
              public String visitTerminal(TerminalNode node) {
                  String text = node.getText();
                  if (!text.equals("<EOF>")) {
                      if (!specialCharacters.contains(text)) {
                          if (!querySeparate.toString().isEmpty() && !specialCharacters.contains(querySeparate.substring(querySeparate.length() - 1))) {
                              querySeparate.append(" "); // Add a space if the last character wasn't a special character
                          }
                           if (text.equals(this.getAlias())) {
                               querySeparate.append(this.getAliasReference());
                           } else {
                           querySeparate.append(text);
                        }
                      } else {
                              querySeparate.append(text);
                          }
                      }
                      return text;
                   }



            // @Override 
            // public String visitAny_name(SQLiteParser.Any_nameContext ctx) { 
            //    if(ctx.IDENTIFIER() != null){
            //       if(ctx.IDENTIFIER().toString().equals(getAlias())){
            //          return this.getAliasReference();
            //       }
            //    }
            //    return visitChildren(ctx);
            //  }
                 
   }
             
         
      //    // Obtener el nodo original
      //         TerminalNodeImpl originalNode = (TerminalNodeImpl) ctx.IDENTIFIER();
      //         System.out.println(originalNode.toString());
      //         if (originalNode.toString().equals(this.alias)) {
	   //            // Obtener el token original
	   //            Token originalToken = originalNode.getSymbol();
	   //            // Crear un nuevo token con el nuevo alias
	   //            CommonToken newAliasToken = new CommonToken(originalToken);
      //            System.out.println("ANTESSSSSSSSSS");
      //            System.out.println(newAliasToken.toString());
	   //            newAliasToken.setText(this.getAliasReference());
      //            System.out.println("yumi");
      //            System.out.println(newAliasToken.toString());
	   //            // Crear un nuevo nodo terminal para el nuevo alias
	   //            TerminalNodeImpl newAliasNode = new TerminalNodeImpl(newAliasToken);
	   //            // Reemplazar el nodo original con el nuevo nodo
	   //            ParserRuleContext parent = ctx.getParent();
	   //            int index = parent.children.indexOf(originalNode);
	   //            parent.children.set(index, newAliasNode);
      //            System.out.println("SERA?>>>>>>>>>>>>>>>>>>>>");
      //            System.out.println(parent.getText());
      //     }
      //     System.out.println("ABEEEEEEEEE");
      //     System.out.println(super.visitAny_name(ctx));
      //     System.out.println();
      //     return super.visitAny_name(ctx);
      // }

     







// -----------------------------
// OTRO SOURCE?

// public String sourceTextForContext(ParseTree context) {
//          Token startToken, stopToken;
//          int stopIndex;
     
//          if (context instanceof ParserRuleContext) {
//              ParserRuleContext ctx = (ParserRuleContext) context;
//              startToken = ctx.getStart();
//              stopToken = ctx.getStop();
//          } else {
//              startToken = ((TerminalNode) context).getSymbol();
//              stopToken = startToken;
//          }
     
//          if (startToken != null) {
//              CharStream cs = startToken.getInputStream();
//              int startIndex = startToken.getStartIndex();
//              stopIndex = (stopToken != null) ? stopToken.getStopIndex() : -1;
             
//              String originalText = cs.getText(new Interval(startIndex, stopIndex));
//             System.out.println("EMPUEZA LO BUENO");
//             System.out.println(originalText.replace("tnm","peneeeeeeee"));
//             String nuevo=originalText.replace("tnm","peneeeeeeee");
//             System.out.println("PASEO");
//             System.out.println(context.toStringTree());
//             System.out.println();
//             System.out.println();
//             System.out.println(context instanceof SQLiteParser.Table_or_subqueryContext);
//             System.out.println();
//             System.out.println();
//             System.out.println(context instanceof SQLiteParser.Any_nameContext);
//             System.out.println();
//             System.out.println();
//             System.out.println(context instanceof SQLiteParser.Result_columnContext);
//              if (context instanceof SQLiteParser.Table_or_subqueryContext) {
//                  SQLiteParser.Table_or_subqueryContext ctxT = (SQLiteParser.Table_or_subqueryContext) context;
//                if (ctxT.table_alias() != null){
//                   if(ctxT.table_alias().any_name().IDENTIFIER().toString().equals(alias)) {
//                      // Realizar cambios en el texto para este nodo Table_or_subquery específico si es necesario
//                      // Por ejemplo, reemplazar 'oldText' con 'newText'
//                      String newText = originalText.replace("oldText", "newText");
//                      return newText;
//                  }
//              }
//             }else if(context instanceof SQLiteParser.Result_columnContext){
//                SQLiteParser.Result_columnContext ctxR = (SQLiteParser.Result_columnContext) context;
//                if (ctxR.column_alias() != null){
//                   if(ctxR.column_alias().IDENTIFIER().toString().equals(alias)) {
//                      System.err.println("COLUMNA");
//                      System.out.println(originalText);
//                      String newText = originalText.replace("oldText", "newText");
//                      return newText;
//                }
//              }
//             }else if(context instanceof SQLiteParser.Any_nameContext){
//                SQLiteParser.Any_nameContext ctxA = (SQLiteParser.Any_nameContext) context;
//                if (ctxA.IDENTIFIER() != null){
//                   if(ctxA.IDENTIFIER().toString().equals(alias)){
//                     String newText = originalText.replace(ctxA.IDENTIFIER().toString(), this.getAliasReference());
//                      return newText;
//                }
//             }
//          }
//              // Si no se cumple la condición anterior, devuelve el texto original del contexto
//              return originalText;
//          }
     
//          return "";
//      }