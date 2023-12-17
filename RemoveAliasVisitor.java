import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.Interval;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.antlr.v4.runtime.tree.TerminalNodeImpl;

/**
 * Clase que implementa un Visitor para el lenguaje SQLite, utilizado para
 * eliminar
 * un alias específico de una consulta.
 */
public class RemoveAliasVisitor extends SQLiteParserBaseVisitor<String> {

  private String alias = " ";
  private String aliasReference = "";
  private StringBuilder querySeparate = new StringBuilder();
  private final Set<String> specialCharacters = new HashSet<>(
      Arrays.asList(".", "(", ")", ","));

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
   * Obtiene la consulta después de la eliminación del alias.
   *
   * @return Consulta sin el alias.
   */
  public String getQuerySeparete() {
    return querySeparate.toString();
  }

  /**
   * Concatena dos resultados de visitas de nodos para formar un resultado
   * agregado.
   *
   * @param aggregate  Resultado agregado actual.
   * @param nextResult Resultado siguiente a agregar.
   * @return Resultado agregado.
   */
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

  /**
   * {@inheritDoc}
   * Visita un nodo que representa una tabla o subconsulta.
   * Si el alias de la tabla coincide con el alias especificado, se elimina de la
   * consulta.
   */
  @Override
  public String visitTable_or_subquery(
      SQLiteParser.Table_or_subqueryContext ctx) {
    if (ctx.table_alias() != null) {
      if (ctx.table_alias().any_name().IDENTIFIER().toString().equals(alias)) {
        visit(ctx.getChild(0));
        return " ";
      }
    }
    return visitChildren(ctx);
  }

  /**
   * {@inheritDoc}
   * Visita un nodo que representa una columna en el resultado de la consulta.
   * Si el alias de la columna coincide con el alias especificado, se elimina de
   * la consulta.
   */
  @Override
  public String visitResult_column(SQLiteParser.Result_columnContext ctx) {
    if (ctx.column_alias() != null) {
      if (ctx.column_alias().IDENTIFIER().toString().equals(alias)) {
        visit(ctx.getChild(0));
        return " ";
      }
    }
    return visitChildren(ctx);
  }

  /**
   * {@inheritDoc}
   * Visita un nodo terminal, como un identificador o un carácter especial.
   * Realiza la lógica para formar la consulta sin el alias.
   */
  @Override
  public String visitTerminal(TerminalNode node) {
    String text = node.getText();
    if (!text.equals("<EOF>")) {
      if (shouldAddSpaceBeforeToken(text)) {
        querySeparate.append(" ");
      }
      if (shouldAddSpaceAfterSpecialCharacter(text)) {
        querySeparate.append(" ");
      }
      querySeparate.append(text.equals(this.getAlias()) ? this.getAliasReference() : text);
    }
    return text;
  }

  private boolean shouldAddSpaceBeforeToken(String text) {
    return !specialCharacters.contains(text) &&
        !querySeparate.toString().isEmpty() &&
        !specialCharacters.contains(querySeparate.substring(querySeparate.length() - 1));
  }

  private boolean shouldAddSpaceAfterSpecialCharacter(String text) {
    return !specialCharacters.contains(text) && !querySeparate.toString().isEmpty() &&
        (endsWithSpecialCharacter(')', ',') && !text.equals(" "));
  }

  private boolean endsWithSpecialCharacter(char... characters) {
    String lastText = querySeparate.toString();
    char lastCharacter = lastText.charAt(lastText.length() - 1);
    for (char character : characters) {
      if (lastCharacter == character) {
        return true;
      }
    }
    return false;
  }
}
