import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
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
    public String visitColumn_alias(SQLiteParser.Column_aliasContext ctx){

		String currentAlias = ctx.IDENTIFIER().getText();

	    // Verificar que el nuevo alias no se repita en la consulta
	    if (currentAlias.equalsIgnoreCase(this.newAlias)) {
	        System.err.println("Error: El nuevo alias ya existe en la consulta.");
	        return null;
	    }

	    if (!esAliasValido(this.newAlias)) {
	        System.err.println("Error: El alias no es válido.");
	        return null;
	    }

	    // Si las verificaciones son exitosas, puedes retornar el texto del contexto
	    return ctx.getText();
    }
    
    public String visitTable_alias(SQLiteParser.Table_aliasContext ctx) {

		String currentAlias = ctx.IDENTIFIER().getText();

	    // Verificar que el nuevo alias no se repita en la consulta
	    if (currentAlias.equalsIgnoreCase(newAlias)) {
	        //lanzamos excepción o mensaje de error?
	        System.err.println("Error: El nuevo alias ya existe en la consulta.");
	        return null; // consultar
	    }

	    if (!esAliasValido(currentAlias)) {
	        System.err.println("Error: El alias no es válido.");
	        return null; //consultar
	    }

	    // Si las verificaciones son exitosas, puedes retornar el texto del contexto
	    return ctx.getText();
    }
    
    public String visitAlias(SQLiteParser.AliasContext ctx) {
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
	
	private boolean esPalabraReservada(String newAlias) {
		boolean reservada = true;
		// Dividir el nuevo alias en palabras
		String[] palabras = newAlias.split("\\s+");

		// Convertir cada palabra a mayúsculas y verificar si es una palabra reservada
		for (int i = 0; i < palabras.length; i++) {
			palabras[i] = palabras[i].toUpperCase();
			switch (palabras[i]) {
				case "SELECT":
				case "FROM":
				case "WHERE":
				case "AS":
				case "JOIN":
				case "ON":
				case "AND":
				case "OR":
				case "NOT":
				case "IN":
				case "LIKE":
				case "BETWEEN":
				case "IS":
				case "NULL":
				case "ORDER":
				case "BY":
				case "GROUP":
				case "HAVING":
				case "UNION":
				case "ALL":
				case "INTERSECT":
				case "EXCEPT":
				case "MINUS":
				case "ANY":
				case "SOME":
				case "EXISTS":
				case "CASE":
				case "WHEN":
				case "THEN":
				case "ELSE":
				case "END":
				case "CREATE":
				case "TABLE":
				case "PRIMARY":
				case "KEY":
				case "*":
					reservada = true;
					break;
				default:
					reservada = false;
					break;
			}
		}
		return reservada;
	}
	
	private boolean esAliasValido(String alias) {

		if ((alias.length() >= 1) && (alias.matches("[a-zA-Z_]+")) && this.esPalabraReservada(alias)){
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
    
