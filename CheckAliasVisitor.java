import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;

public class CheckAliasVisitor extends SQLiteParserBaseVisitor<String> {

	// alias que se quiere cambiar
	private String alias;
	// nuevo alias
	private String newAlias;
	private boolean esValido = true;

	public CheckAliasVisitor(String alias, String newAlias) {
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
			return aggregate; // Si el resultado acumulado es null, simplemente devuelve el resultado del nodo
								// hijo
		} else {
			return aggregate + nextResult; // Concatena los resultados
		}
	}

	@Override
	public String visitColumn_alias(SQLiteParser.Column_aliasContext ctx) {
		// System.err.println("VISIT COLUMN ALIAS");
		if (this.esValido) {
			String currentAlias = ctx.IDENTIFIER().getText();
			// Verificar que el nuevo alias no se repita en la consulta
			this.existInTheQuery(currentAlias);
		}
		// Retornar el texto del contexto si las verificaciones son exitosas
		return ctx.getText();
	}

	@Override
	public String visitTable_alias(SQLiteParser.Table_aliasContext ctx) {
		// System.err.println("VISIT TABLE ALIAS");
		if (this.esValido) {
			String currentAlias = ctx.any_name().getChild(0).getText();
			// Verificar que el nuevo alias no se repita en la consulta
			this.existInTheQuery(currentAlias);
		}

		// Retornar el texto del contexto si las verificaciones son exitosas
		return ctx.getText();
	}

	@Override
	public String visitColumn_name(SQLiteParser.Column_nameContext ctx) {
		// System.err.println("VISIT COLUMN ALIAS");
		if (this.esValido) {
			String currentAlias = ctx.any_name().getChild(0).getText();
			// Verificar que el nuevo alias no se repita en la consulta
			this.existInTheQuery(currentAlias);
		}
		// Retornar el texto del contexto si las verificaciones son exitosas
		return ctx.getText();
	}

	@Override
	public String visitTable_name(SQLiteParser.Table_nameContext ctx) {
		System.err.println("VISIT TABLE NAME");
		if (this.esValido) {
			String currentAlias = ctx.any_name().getChild(0).getText();
			// Verificar que el nuevo alias no se repita en la consulta
			this.existInTheQuery(currentAlias);
		}
		// Retornar el texto del contexto si las verificaciones son exitosas
		return ctx.getText();
	}

	public void existInTheQuery(String currentAlias) {
		// Verificar que el nuevo alias no se repita en la consulta
		if (currentAlias.equalsIgnoreCase(this.newAlias)) {
			System.err.println("Error: El nuevo alias ya existe en la consulta.");
			this.esValido = false;
		}
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
}