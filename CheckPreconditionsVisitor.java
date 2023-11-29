import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import org.antlr.v4.runtime.tree.ParseTree;


import sqlitegrammar.*;

public class CheckPreconditionsVisitor extends SQLiteParserBaseVisitor<String> {

	// alias que se quiere cambiar
	private String alias;
	// nuevo alias
	private String newAlias;
	
    public CheckPreconditionsVisitor(String alias, String newAlias) {
		super();
		this.alias = alias;
		this.newAlias = newAlias;
	}

    @Override
    public String visitColumn_alias(SQLiteParser.Column_aliasContext ctx) {
        String currentAlias = ctx.IDENTIFIER().getText();

        // Verificar que el nuevo alias no se repita en la consulta
        if (currentAlias.equalsIgnoreCase(this.newAlias)) {
            System.err.println("Error: El nuevo alias ya existe en la consulta.");
            return null;  
        }

        // Retornar el texto del contexto si las verificaciones son exitosas
        return ctx.getText();
    }

    @Override
    public String visitTable_alias(SQLiteParser.Table_aliasContext ctx) {
        String currentAlias = ctx.IDENTIFIER().getText();

        // Verificar que el nuevo alias no se repita en la consulta
        if (currentAlias.equalsIgnoreCase(this.newAlias)) {
            System.err.println("Error: El nuevo alias ya existe en la consulta.");
            return null; 
        }

        // Retornar el texto del contexto si las verificaciones son exitosas
        return ctx.getText();
    }
    
    public String visitAlias(SQLiteParser.AliasContext ctx) {
    	
    	String originalAlias = ctx.IDENTIFIER(0).getText();
    	
    	//verifica que el alias de la consulta sea el mismo que el que se quiere cambiar
    	if (!originalAlias.equals(this.alias)) {
            return null;
    	}
    	
    	// Verificar que el nuevo alias no se repita en la consulta
        if  (newAlias.equalsIgnoreCase(this.newAlias)) {
            System.err.println("Error: El nuevo alias ya existe en la consulta.");
            return null;
        }
    	
    	return ctx.getText();
    }
    
    public boolean aliasExist(String query, String alias) {
		if (query.toUpperCase().contains(" " + alias.toUpperCase() + " AS")){
			return true;
		}
		return false;
	}

	public boolean newAliasNotExist(String query, String alias) {
		if (!query.toUpperCase().contains(" " + alias.toUpperCase() + " AS")){
			return true;
		}
		return false;
	}

    
    /* 
     * // Que no exista otro alias igual al que quiero cambiar
		visitor.visitAliasNotExist(newParseTree);

		// No utilizar palabras reservadas CONSULTAR
		// se chequea al setear el nuevo alias

		// Que el nuevo alias no exista
		visitor.visitNewAliasNotExist(newParseTree);

		// Que el nuevo alias no sea igual al nombre de la tabla o de otra columna
		visitor.visitNewAliasNotEqualTable(newParseTree);
		visitor.visitNewAliasNotEqualColumn(newParseTree);

		// Que el cambio no genere conflictos en la subquery
     */
}
    
