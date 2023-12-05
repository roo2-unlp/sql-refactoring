import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

//import sqlitegrammar.SQLiteParser;
//import sqlitegrammar.SQLiteParserBaseVisitor;

public class PreConditionsVisitor extends SQLiteParserBaseVisitor<Void>{
	private boolean containsGroup;
	private boolean containsAggregateFunction;
	private boolean containsDistinct;
	
	public PreConditionsVisitor() {
		containsGroup = false;
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
	
	public boolean getContainsAggregateFunction() {
		return this.containsAggregateFunction;
	}
	
	public boolean getContainsDistinct() {
		return this.containsDistinct;
	}
	
	public boolean getPrecondicion() {
		return this.getContainsGroup() && !this.getContainsAggregateFunction() && !this.getContainsDistinct();
	}
	
	public boolean getPostcondicion() {
		return !this.getContainsGroup() && !this.getContainsAggregateFunction() && this.getContainsDistinct();
	}
}
