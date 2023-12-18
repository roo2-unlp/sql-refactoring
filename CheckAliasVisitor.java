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
			return aggregate; 
		} else {
			return aggregate + nextResult;
		}
	}

	@Override
	public String visitColumn_alias(SQLiteParser.Column_aliasContext ctx) {
		if (this.esValido) {
			String currentAlias = ctx.IDENTIFIER().getText();
			this.existInTheQuery(currentAlias);
		}
		return ctx.getText();
	}

	@Override
	public String visitTable_alias(SQLiteParser.Table_aliasContext ctx) {
		if (this.esValido) {
			String currentAlias = ctx.any_name().getChild(0).getText();
			this.existInTheQuery(currentAlias);
		}
		return ctx.getText();
	}

	@Override
	public String visitColumn_name(SQLiteParser.Column_nameContext ctx) {
		if (this.esValido) {
			String currentAlias = ctx.any_name().getChild(0).getText();
			this.existInTheQuery(currentAlias);
		}
		return ctx.getText();
	}

	@Override
	public String visitTable_name(SQLiteParser.Table_nameContext ctx) {
		if (this.esValido) {
			String currentAlias = ctx.any_name().getChild(0).getText();
			this.existInTheQuery(currentAlias);
		}
		return ctx.getText();
	}

	public void existInTheQuery(String currentAlias) {
		if (currentAlias.equalsIgnoreCase(this.newAlias)) {
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