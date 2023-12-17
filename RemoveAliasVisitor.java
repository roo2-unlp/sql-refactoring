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
      private final Set<String> specialCharacters = new HashSet<>(Arrays.asList(".", "(",")", ","));

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
      
         
   
            @Override
            public String  visitTable_or_subquery(SQLiteParser.Table_or_subqueryContext ctx) {
             if(ctx.table_alias() != null){ 
              if(ctx.table_alias().any_name().IDENTIFIER().toString().equals(alias)) {
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
                     if (!specialCharacters.contains(text) && !querySeparate.toString().isEmpty() && !specialCharacters.contains(querySeparate.substring(querySeparate.length() - 1))) {
                        querySeparate.append(" ");
                     }
                     if (!specialCharacters.contains(text) && !querySeparate.toString().isEmpty()) {
                        String lastText = querySeparate.toString();
                        char lastCharacter = lastText.charAt(lastText.length() - 1);
                        if ((lastCharacter == ')' || (lastCharacter == ',')) && !text.equals(" ")) {
                            querySeparate.append(" ");
                        }
                    }
                     if (text.equals(this.getAlias())) {
                        querySeparate.append(this.getAliasReference());
                     } else {
                        querySeparate.append(text);

                     }          
                  }
                  return text;
               } 
                   
   }
             
     