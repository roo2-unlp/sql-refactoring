import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import sqlitegrammar.SQLiteParser;
import sqlitegrammar.SQLiteParserBaseVisitor;

public class ConditionVisitor extends SQLiteParserBaseVisitor<Void>{
	private boolean tieneGroupBy;
	private boolean tieneFuncionAgregacion;
	private boolean tieneDistinct;
	
	public ConditionVisitor() {
		tieneGroupBy = false;
		tieneFuncionAgregacion = false;
		tieneDistinct = false;
	}
		
	@Override 
    public Void visitSelect_core(SQLiteParser.Select_coreContext ctx) { 
		for (ParseTree hijo : ctx.children) {
            if (hijo != null && (hijo instanceof TerminalNode)) {
            	TerminalNode nodoTerminal = (TerminalNode) hijo;
            	if (nodoTerminal.getSymbol().getType() == SQLiteParser.GROUP_) {
            		tieneGroupBy = true;
            	} 
            	if (nodoTerminal.getSymbol().getType() == SQLiteParser.DISTINCT_) {
            		tieneDistinct = true;
            	}   
            }
        }
		return super.visitChildren(ctx);
    }
	
	@Override 
	public Void visitFunction_name(SQLiteParser.Function_nameContext ctx) { 
		if(ctx.getChild(0).getChild(0) != null) {
			tieneFuncionAgregacion = true;
		}	
		return visitChildren(ctx); 
	}
	
	public boolean getTieneGroupBy() {
		return this.tieneGroupBy;
	}
	
	public boolean getTieneFuncionAgregacion() {
		return this.tieneFuncionAgregacion;
	}
	
	public boolean getTieneDistinct() {
		return this.tieneDistinct;
	}
	
	public boolean getPrecondicion() {
		return this.getTieneGroupBy() && !this.getTieneFuncionAgregacion() && !this.getTieneDistinct();
	}
	
	public boolean getPostcondicion() {
		return !this.getTieneGroupBy() && !this.getTieneFuncionAgregacion() && this.getTieneDistinct();
	}
}
