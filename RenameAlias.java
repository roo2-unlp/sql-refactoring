import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import sqlitegrammar.*;

public class RenameAlias extends Refactoring {
	// alias que se quiere cambiar
	private String alias;
	// nuevo alias
	private String newAlias;
	// texto de la consulta antes de aplicar el refactoring
	// se usa para comparar luego con las postcondiciones
	private String preconditionText;

	public void setAlias(String alias, String newAlias) {
		this.alias = alias;
		if (!esPalabraReservada(newAlias)) {
			this.newAlias = newAlias;
		} else
			this.newAlias = alias;
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

	public boolean aliasExist(String query, String alias) {
		return true;
	}

	public boolean newAliasNotExist(String query, String alias) {
		return true;
	}

	private SQLiteParser createSQLiteParser(String text) {
		// creación de un SQLiteParser para analizar un texto dado
		CharStream charStream = CharStreams.fromString(text);
		SQLiteLexer lexer = new SQLiteLexer(charStream);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		return new SQLiteParser(tokens);
	}

	@Override
	protected boolean checkPreconditions(String text) {
		// Creo un parser para analizar el texto
		SQLiteParser parser = this.createSQLiteParser(text);
		// Creo un árbol de análisis sintáctico
		ParseTree newParseTree = parser.parse();

		// Si hay errores de sintaxis, el método retorna false
		if (parser.getNumberOfSyntaxErrors() > 0) {
			preconditionText = null;
			return false;
		}

		// Guardo el texto de la consulta
		preconditionText = newParseTree.getText();

		// Creo un visitor para recorrer el árbol de análisis sintáctico
		AliasVisitor visitor = new AliasVisitor();

		// Visito el árbol de análisis sintáctico
		// Que la consulta tenga un alias
		visitor.visitAliasExist(newParseTree);

		// Que no exista otro alias igual al que quiero cambiar
		visitor.visitAliasNotExist(newParseTree);

		// No utilizar palabras reservadas CONSULTAR
		// se chequea al setear el nuevo alias

		// Que el nuevo alias no exista
		visitor.visitNewAliasNotExist(newParseTree);

		// Que el nuevo alias no sea igual al nombre de la tabla o de otra columna
		visitor.visitNewAliasNotEqualTable(newParseTree);
		visitor.visitNewAliasNotEqualColumn(newParseTree);

		// Que el cambio no genere conflictos en la subquery
		return true;
	}

	@Override
	protected String transform(String text) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected boolean checkPostconditions(String text) {
		//Si la precondición es null directamente no pasó las precondiciones
		//y por ende las postcondiciones tampoco
		if (preconditionText == null) {
            return false;
        }
		//Sino se compara el texto de la consulta antes y después del refactoring
		// Que el alias haya sido modificado en todos los lugares y no esté el anterior
		else
        	return !(preconditionText.equals(text)) && 
			metodo que chequea que el alias haya sido modificado en todos los lugares;
	}

}
