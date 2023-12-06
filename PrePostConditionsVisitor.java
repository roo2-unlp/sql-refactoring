import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

public class PrePostConditionsVisitor extends SQLiteParserBaseVisitor<Void>{
	private boolean containsGroup;
	private boolean containsBy;
	private boolean containsAggregateFunction;
	private boolean containsDistinct;
	
	public PrePostConditionsVisitor() {
		containsGroup = false;
		containsBy = false;
		containsAggregateFunction = false;
		containsDistinct = false;
	}
		
	@Override 
    public Void visitSelect_core(SQLiteParser.Select_coreContext ctx) { 
		for (ParseTree hijo : ctx.children) {
            if (hijo != null && (hijo instanceof TerminalNode)) {
            	TerminalNode nodoTerminal = (TerminalNode) hijo;
            	if (nodoTerminal.getSymbol().getType() == SQLiteParser.GROUP_) {
            		containsGroup = true;
            	} 
				if (nodoTerminal.getSymbol().getType() == SQLiteParser.BY_) {
            		containsBy = true;
            	}
            	if (nodoTerminal.getSymbol().getType() == SQLiteParser.DISTINCT_) {
            		containsDistinct = true;
            	}   
            }
        }
		return super.visitChildren(ctx);
    }
	
	@Override 
	public Void visitFunction_name(SQLiteParser.Function_nameContext ctx) { 
		if(ctx.getChild(0).getChild(0) != null) {
			containsAggregateFunction = true;
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
	
}
