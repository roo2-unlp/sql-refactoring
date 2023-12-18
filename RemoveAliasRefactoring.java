import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

/**
 * Clase que implementa un refactoring para eliminar un alias específico de una
 * consulta SQL.
 */
public class RemoveAliasRefactoring extends Refactoring {
    private String alias = "";
    private String aliasReference = "";

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
        return this.alias;
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
        return this.aliasReference;
    }

    /**
     * Crea un analizador sintáctico SQLiteParser a partir de un texto de consulta.
     *
     * @param text Texto de la consulta.
     * @return Objeto SQLiteParser.
     */
    private SQLiteParser createSQLiteParser(String text) {
        CharStream charStream = CharStreams.fromString(text);
        SQLiteLexer lexer = new SQLiteLexer(charStream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        return new SQLiteParser(tokens);
    }

    /**
     * Verifica las precondiciones antes de aplicar el refactoring.
     *
     * @param text Texto de la consulta.
     * @return true si las precondiciones son satisfactorias, false de lo contrario.
     */
    protected boolean checkPreconditions(String text) {
        SQLiteParser parser = this.createSQLiteParser(text);
        ParseTree newParseTree = parser.parse();
        if (parser.getNumberOfSyntaxErrors() > 0) {
            return false;
        }

        AliasCheckerVisitor visitorCheck = new AliasCheckerVisitor();
        visitorCheck.setAlias(getAlias());
        visitorCheck.visit(newParseTree);

        if (visitorCheck.getAliasEncontrado()) {
            newParseTree.getText();
            this.setAliasReference(visitorCheck.getAliasReference());
            return true;
        } else {
            return false;
        }
    }

    /**
     * Realiza la transformación del texto de la consulta aplicando el refactoring.
     *
     * @param text Texto de la consulta.
     * @return Texto transformado después de aplicar el refactoring.
     */
    protected String transform(String text) {
        SQLiteParser parser = this.createSQLiteParser(text);
        ParseTree tree = parser.parse();
        RemoveAliasVisitor visitor = new RemoveAliasVisitor();
        visitor.setAlias(alias);
        visitor.setAliasReference(aliasReference);
        visitor.visit(tree);
        return visitor.getQuerySeparete();
    }

    /**
     * Verifica las postcondiciones después de aplicar el refactoring.
     *
     * @param text Texto de la consulta transformada.
     * @return true si las postcondiciones son satisfactorias, false de lo
     *         contrario.
     */
    protected boolean checkPostconditions(String text) {
        SQLiteParser parser = this.createSQLiteParser(text);
        ParseTree tree = parser.parse();
        AliasCheckerVisitor visitor = new AliasCheckerVisitor();
        visitor.visit(tree);
        return !visitor.getAliasEncontrado();
    }

}
