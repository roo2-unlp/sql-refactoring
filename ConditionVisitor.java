import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import sqlitegrammar.SQLiteParser;
import sqlitegrammar.SQLiteParser.ExprContext;
import sqlitegrammar.SQLiteParser.Result_columnContext;
import sqlitegrammar.SQLiteParserBaseVisitor;

public class ConditionVisitor extends SQLiteParserBaseVisitor<Void>{
	private boolean tieneGroupBy;
	private boolean tieneFuncionAgregacion;
	private boolean tieneDistinct;
	private boolean tieneMismasColumnas;
	private List<String> columnasSelect;
	private List<String> columnasGroup;
	
	public ConditionVisitor() {
		tieneGroupBy = false;
		tieneFuncionAgregacion = false;
		tieneDistinct = false;
		tieneMismasColumnas = false;
		columnasSelect = new ArrayList<String>();
		columnasGroup = new ArrayList<String>();
	}
	
	
	/* Metodo utilizado para recorrer las distintas partes del SELECT
	 * y poder verificar las distintas condiciones segun sea utlizado
	 * por la pre o post condicion*/
	@Override 
    public Void visitSelect_core(SQLiteParser.Select_coreContext ctx) {

		tieneGroupBy = false;
		tieneFuncionAgregacion = false;
		tieneDistinct = false;
		tieneMismasColumnas = false;
		columnasSelect.clear();
		columnasGroup.clear();
	
		//iterando los hijos del sub-arbol select_core
		for (ParseTree hijo : ctx.children) {
            if (hijo != null && (hijo instanceof TerminalNode)) {
            	TerminalNode nodoTerminal = (TerminalNode) hijo;
            	if (nodoTerminal.getSymbol().getType() == SQLiteParser.GROUP_) {
            		tieneGroupBy = true;
            		// lista que guarda las columnas de groupBy
            		for (ExprContext columna : ctx.groupByExpr) {
            			if(columna.getChild(2) != null) {
            				//tiene alias
            				// accedo a expr -> column_name -> any_name -> [campo]
            				columnasGroup.add(columna.getChild(2).getText());
            			}
            			else {
            				columnasGroup.add(columna.getText());
            			}
            		}
            	} 
            	
            	if (nodoTerminal.getSymbol().getType() == SQLiteParser.DISTINCT_) {
            		tieneDistinct = true;
            	}  
            	
            	if (nodoTerminal.getSymbol().getType() == SQLiteParser.SELECT_) {
            		// lista que guarda las columnas de select
            		for (Result_columnContext columna : ctx.result_column()) {
            			if((columna.getChild(0) != null) && (columna.getText().equals("*"))) {
            				// tiene asterisco, no se puede refactorizar
            				break;
            			}
            			
            			if((columna.getChild(2) != null) && (!tieneFuncionAgregacion)) {
            				//tiene alias (AS)
            				// accedo a expr -> column_name -> any_name -> [campo]
            				if(columna.getChild(0).getChild(2) != null) {
            					//tiene alias por ejemplo d.nombre
            					columnasSelect.add(columna.getChild(0).getChild(2).getText());
            				}
            				else {
            					// no tiene alias por ejemplo nombre
            					columnasSelect.add(columna.getChild(0).getText());
            				}           				
            			}
            			else {
            				columnasSelect.add(columna.getText());
            			}
            		}
            	}     	
            }
        }				

		//condicion para ingresar al metodo de comparacion de listas solo en pre condicion
		tieneMismasColumnas = this.verificarListas();
		
		return super.visitChildren(ctx);
    }
	
	
	private boolean verificarListas() {

	    // Verificar si las listas tienen la misma longitud
	    if (this.columnasSelect.size() != this.columnasGroup.size()) {
	        return false;
	    }

	    // Ordenar ambas listas
	    Collections.sort(this.columnasSelect);
	    Collections.sort(this.columnasGroup);
	    
	    // Comparar las listas ordenadas
	    return this.columnasSelect.equals(this.columnasGroup);
	}
	
	//Metodo que visita los nodos que representan funciones de agregaciòn
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
	
	public boolean getTieneMismasColumnas() {
		return this.tieneMismasColumnas;
	}
	
	//devuelve los resultados luego de recorrer y analizar las pre condiciones
	public boolean getPrecondicion() {
		return this.getTieneGroupBy() && !this.getTieneFuncionAgregacion() 
			  && !this.getTieneDistinct() && this.getTieneMismasColumnas();
	}
	
	//devuelve los resultados luego de recorrer y analizar las post condiciones
	public boolean getPostcondicion() {
		return !this.getTieneGroupBy() && !this.getTieneFuncionAgregacion() 
				&& this.getTieneDistinct();
	}
}