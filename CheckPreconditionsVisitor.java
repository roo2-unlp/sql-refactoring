import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;

public class CheckPreconditionsVisitor extends SQLiteParserBaseVisitor<String> {

	// alias que se quiere cambiar
	private String alias;
	// nuevo alias
	private String newAlias;
	private boolean esValido = false;

	public CheckPreconditionsVisitor(String alias, String newAlias) {
		super();
		this.alias = alias;
		this.newAlias = newAlias;
	}
	
	@Override
    protected String defaultResult() {
        return "";
    }

    @Override
    protected String aggregateResult(String aggregate, String nextResult) {
        if (aggregate == null) {
            return aggregate;  // Si el resultado acumulado es null, simplemente devuelve el resultado del nodo hijo
        } else {
            return aggregate + nextResult;  // Concatena los resultados
        }
    }

    @Override
    public String visitColumn_alias(SQLiteParser.Column_aliasContext ctx) {
        //System.err.println("VISIT COLUMN ALIAS");
        String currentAlias = ctx.IDENTIFIER().getText();
        // Verificar que el nuevo alias no se repita en la consulta
        if (currentAlias.equalsIgnoreCase(this.newAlias)) {
            System.err.println("Error: El nuevo alias ya existe en la consulta.");
            this.esValido = false;
            return ctx.getText();
        }
        this.esValido = true;

        // Retornar el texto del contexto si las verificaciones son exitosas
        return ctx.getText();
    }

    @Override
    public String visitTable_alias(SQLiteParser.Table_aliasContext ctx) {
    	//System.err.println("VISIT TABLE ALIAS");
        String currentAlias = ctx.any_name().getChild(0).getText();

        // Verificar que el nuevo alias no se repita en la consulta
        if (currentAlias.equalsIgnoreCase(this.newAlias)) {
            System.err.println("Error: El nuevo alias ya existe en la consulta.");
            this.esValido = false;
            return ctx.getText();
        }
        this.esValido = true;

        // Retornar el texto del contexto si las verificaciones son exitosas
        return ctx.getText();
    }

    public boolean aliasExist(String query, String alias) {
		if (query.toUpperCase().contains(" " + alias.toUpperCase() + " AS")){
			return true;
		}
		return false;
	}

	public boolean newAliasNotExist(String query) {
		if (!query.toUpperCase().contains(" " + newAlias.toUpperCase() + " AS")) {
			return true;
		}
		return false;
	}
	
	
	public String getAlias() {
		return alias;
	}

	public String getNewAlias() {
		return newAlias;
	}

	public boolean getEsValido() {
		return this.esValido;
	}

	/*
	 * // Que no exista otro alias igual al que quiero cambiar
	 * visitor.visitAliasNotExist(newParseTree);
	 *
	 * // No utilizar palabras reservadas CONSULTAR
	 * // se chequea al setear el nuevo alias
	 *
	 * // Que el nuevo alias no exista
	 * visitor.visitNewAliasNotExist(newParseTree);
	 *
	 * // Que el nuevo alias no sea igual al nombre de la tabla o de otra columna
	 * visitor.visitNewAliasNotEqualTable(newParseTree);
	 * visitor.visitNewAliasNotEqualColumn(newParseTree);
	 *
	 * // Que el cambio no genere conflictos en la subquery
	 */
}