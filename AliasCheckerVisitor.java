
/**
 * Clase que implementa un Visitor para el lenguaje SQLite, utilizado para
 * verificar la presencia de un alias específico en una consulta y obtener
 * la tabla o columna a la que hace referencia.
 */
public class AliasCheckerVisitor extends SQLiteParserBaseVisitor<String> {
   private String alias = null;
   private String aliasReference = null;
   private boolean aliasEncontrado = false;

   /**
    * Establece el alias que se eliminará de la consulta.
    *
    * @param alias Alias a eliminar.
    */
   public void setAlias(String alias) {
      this.alias = alias;
   }

   /**
    * Obtiene el alias que se eliminará de la consulta.
    *
    * @return Alias a eliminar.
    */
   public String getAlias() {
      return alias;
   }

   /**
    * Establece la tabla o columna a la que hace referencia el alias que se
    * eliminará de la consulta.
    *
    * @param aliasReference Tabla o columna a la que hace referencia el alias.
    */
   public void setAliasReference(String aliasReference) {
      this.aliasReference = aliasReference;
   }

   /**
    * Obtiene la tabla o columna a la que hace referencia el alias que se eliminará
    * de la consulta.
    *
    * @return Tabla o columna a la que hace referencia el alias.
    */
   public String getAliasReference() {
      return aliasReference;
   }

   /**
    * Verifica si el alias buscado fue encontrado en la consulta.
    *
    * @return true si el alias fue encontrado, false de lo contrario.
    */
   public boolean getAliasEncontrado() {
      return aliasEncontrado;
   }

   private void aliasEncontrado() {
      this.aliasEncontrado = true;
   }

   /**
    * {@inheritDoc}
    * Visita un nodo de expresión en la consulta y verifica si contiene el alias
    * buscado.
    *
    * @param ctx Contexto del nodo.
    * @return Resultado de la visita.
    */
   @Override
   public String visitExpr(SQLiteParser.ExprContext ctx) {
      if (ctx.table_name() != null) {
         if (ctx.table_name().any_name().IDENTIFIER().toString().equals(this.getAlias())) {
            this.aliasEncontrado();
         }
      }
      return super.visitExpr(ctx);
   }

   /**
    * {@inheritDoc}
    * Visita un nodo que representa una tabla o subconsulta y verifica si contiene
    * el alias buscado.
    *
    * @param ctx Contexto del nodo.
    * @return Resultado de la visita.
    */
   @Override
   public String visitTable_or_subquery(SQLiteParser.Table_or_subqueryContext ctx) {
      if (ctx.table_alias() != null) {
         if (ctx.table_alias().any_name().IDENTIFIER().toString().equals(this.getAlias())) {
            this.aliasEncontrado();
            this.setAliasReference(ctx.table_name().any_name().IDENTIFIER().toString());
         }
      }
      return super.visitTable_or_subquery(ctx);
   }

   /**
    * {@inheritDoc}
    * Visita un nodo que representa un restricción de unión (JOIN) y verifica si
    * contiene el alias buscado.
    *
    * @param ctx Contexto del nodo.
    * @return Resultado de la visita.
    */
   @Override
   public String visitJoin_constraint(SQLiteParser.Join_constraintContext ctx) {
      if (ctx.expr().column_name() != null) {
         if (ctx.expr().column_name().any_name().IDENTIFIER().toString().equals(this.getAlias())) {
            this.aliasEncontrado();
            this.setAliasReference(ctx.expr().table_name().any_name().IDENTIFIER().toString());
         }
      }

      return super.visitJoin_constraint(ctx);
   }

   /**
    * {@inheritDoc}
    * Visita un nodo que representa una columna en el resultado de la consulta y
    * verifica si contiene el alias buscado.
    *
    * @param ctx Contexto del nodo.
    * @return Resultado de la visita.
    */
   @Override
   public String visitResult_column(SQLiteParser.Result_columnContext ctx) {
      if (ctx.table_name() != null) {
         if (ctx.table_name().any_name().IDENTIFIER().toString().equals(this.getAlias())) {
            this.aliasEncontrado();
         }
      }
      if (ctx.column_alias() != null) {
         if (ctx.column_alias().IDENTIFIER().toString().equals(this.getAlias())) {
            this.aliasEncontrado();
         }
      }

      return super.visitResult_column(ctx);
   }

   /**
    * {@inheritDoc}
    * Visita un nodo que representa un término de ordenamiento (ORDER BY) en la
    * consulta y verifica si contiene el alias buscado.
    *
    * @param ctx Contexto del nodo.
    * @return Resultado de la visita.
    */
   @Override
   public String visitOrdering_term(SQLiteParser.Ordering_termContext ctx) {

      if (ctx.expr().table_name() != null) {
         if (ctx.expr().table_name().any_name().IDENTIFIER().toString().equals(this.getAlias())) {
            this.aliasEncontrado();
         }
      }

      return super.visitOrdering_term(ctx);
   }

}
