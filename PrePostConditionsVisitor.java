import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

public class PrePostConditionsVisitor extends SQLiteParserBaseVisitor<Void>{
	private boolean containsGroup;
	private boolean containsBy;
	private boolean containsAggregateFunction;
	private boolean containsDistinct;
	private boolean containsStar;
	
	public PrePostConditionsVisitor() {
		containsGroup = false;
		containsBy = false;
		containsAggregateFunction = false;
		containsDistinct = false;
		containsStar = false;
	}
		
	@Override 
    public Void visitSelect_core(SQLiteParser.Select_coreContext ctx) { 
		// Utiliza los métodos proporcionados por ANTLR para verificar la presencia de ciertos tokens
		if (ctx.GROUP_() != null) {
			containsGroup = true;
		}
		if (ctx.BY_() != null) {
			containsBy = true;
		}
		if (ctx.DISTINCT_() != null) {
			containsDistinct = true;
		}

		return visitChildren(ctx);
    }
	
	@Override 
	public Void visitFunction_name(SQLiteParser.Function_nameContext ctx) { 
		if(ctx.getChild(0).getChild(0) != null) {
			containsAggregateFunction = true;
		}	
		return visitChildren(ctx); 
	}

	@Override
    public Void visitResult_column(SQLiteParser.Result_columnContext ctx) {
		if (ctx.STAR() != null) {
			containsStar = true;
		}
		return visitChildren(ctx);
	}
	
	public boolean getContainsGroup() {
		return this.containsGroup;
	}

	public boolean getContainsBy() {
		return this.containsBy;
	}

	public boolean getContainsGroupBy() {
		return this.containsGroup && this.containsBy;
	}
	
	public boolean getContainsAggregateFunction() {
		return this.containsAggregateFunction;
	}
	
	public boolean getContainsDistinct() {
		return this.containsDistinct;
	}

	public boolean getContainsStar() {
		return this.containsStar;
	}
	
}
