


public class AliasCheckerVisitor  extends SQLiteParserBaseVisitor<String>{
       private String alias=null;
       private String aliasReference=null;
       private boolean aliasEncontrado=false;

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
      public boolean getAliasEncontrado(){
        return aliasEncontrado;
      }
      private void aliasEncontrado(){
        this.aliasEncontrado=true;
      }

      @Override   
      public String visitExpr(SQLiteParser.ExprContext ctx) {
         if (ctx.table_name() != null){        
            if(ctx.table_name().any_name().IDENTIFIER().toString().equals(this.getAlias())) {
               this.aliasEncontrado();
            } 
      }       
         return super.visitExpr(ctx);
      }
      //esto tendra que devolver el contexto entero y solo cambiar el valor de alias encontrado si es que existe en las expr
      @Override
      public String visitTable_or_subquery(SQLiteParser.Table_or_subqueryContext ctx) {      
        if (ctx.table_alias() != null){
           if( ctx.table_alias().any_name().IDENTIFIER().toString().equals(this.getAlias())) {
               this.aliasEncontrado();
               this.setAliasReference(ctx.table_name().any_name().IDENTIFIER().toString());
            }     
      }  
         return super.visitTable_or_subquery(ctx);
       // este si encuentra el alias cambia el booleano y ademas se queda con el nombre de la tabla
      }

     
    
       @Override 
       public String visitJoin_constraint(SQLiteParser.Join_constraintContext ctx) {
         if(ctx.expr().column_name()  != null){
           if(ctx.expr().column_name().any_name().IDENTIFIER().toString().equals(this.getAlias())){
               this.aliasEncontrado();
               this.setAliasReference(ctx.expr().table_name().any_name().IDENTIFIER().toString());
            }
      }
         
      
         return super.visitJoin_constraint(ctx);
       // este si encuentra el alias cambia el booleano y ademas se queda con el nombre de la tabla
       }

      @Override 
      public String visitResult_column(SQLiteParser.Result_columnContext ctx) {
            if(ctx.table_name() != null){
              if( ctx.table_name().any_name().IDENTIFIER().toString().equals(this.getAlias())){
                  this.aliasEncontrado();
               }
            }
            if(ctx.column_alias() != null ){
               if(ctx.column_alias().IDENTIFIER().toString().equals(this.getAlias())){
                  //this.setAliasReference(ctx.table_name().any_name().IDENTIFIER().toString());
                  this.aliasEncontrado();
            }
         } 
            
            return super.visitResult_column(ctx);
         }// aca busca si existe el alias dentro de la sentencia select y si el alias es de una columna se queda
	
       
      @Override 
      public String visitOrdering_term(SQLiteParser.Ordering_termContext ctx) { 
         
         if(ctx.expr().table_name() != null){
           if(ctx.expr().table_name().any_name().IDENTIFIER().toString().equals(this.getAlias())){
               this.aliasEncontrado(); 
            }
      }
         
         return super.visitOrdering_term(ctx);
        }


    
      }

