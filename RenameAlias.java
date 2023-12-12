import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;

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
		this.newAlias = newAlias;
	}

	private SQLiteParser createSQLiteParser(String text) {
		CharStream charStream = CharStreams.fromString(text);
		SQLiteLexer lexer = new SQLiteLexer(charStream);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		return new SQLiteParser(tokens);
	}

	protected boolean checkPreconditions(String text) {
		SQLiteParser parser = this.createSQLiteParser(text);
		ParseTree newParseTree = parser.parse();

		CheckPreconditionsVisitor visitor = new CheckPreconditionsVisitor(this.alias, this.newAlias);
		String checkedQuery = visitor.visit(newParseTree);
		// System.out.println("Imprimiendo checked query:" + checkedQuery);

		if (this.esAliasValido(this.alias) && this.esAliasValido(this.newAlias) &&
				(parser.getNumberOfSyntaxErrors() == 0) && (visitor.getEsValido())) {
			// Guardo el texto de la consulta
			System.out.println("chepreconditions se está ejecutando el if");
			preconditionText = newParseTree.getText();
			// System.out.println("PRECONDITION TEXT: " + preconditionText);
			return true;
		}
		// System.out.println("PRECONDITION TEXT: " + preconditionText);
		preconditionText = null;
		return false;
	}

	@Override
	protected String transform(String text) {
		SQLiteParser parser = this.createSQLiteParser(text);
		ParseTree tree = parser.parse();

		TransformAliasVisitor visitor = new TransformAliasVisitor(this.alias, this.newAlias);
		visitor.visit(tree);
		ParseTree newTree = tree;
		TransformAliasVisitor visitor1 = new TransformAliasVisitor(this.alias, this.newAlias);
		// visitor1.visit(tree);
		visitor1.visit(newTree);
		System.out.println("ARBOL VISITADO en string:  " + visitor1.getSeparatedWords());
		return visitor1.getSeparatedWords();
	}

	@Override
	protected boolean checkPostconditions(String text) {
		// Si la precondición es null directamente no pasó las precondiciones
		// y por ende las postcondiciones tampoco
		if (preconditionText == null) {
			return false;
		}
		// Sino se compara el texto de la consulta antes y después del refactoring
		// Que el alias haya sido modificado en todos los lugares y no esté el anterior
		else {
			// return !(preconditionText.equals(text));
			// && metodo que chequea que el alias haya sido modificado en todos los lugares;
			if (!(preconditionText.equals(this.transform(text)))) {
				return true;
			}
			System.out.println("checkpostconditions se está ejecutando");
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
		if ((alias.length() >= 1) && (alias.matches("[a-zA-Z_]+")) && !this.esPalabraReservada(alias)) {
			return true;
		}
		return false;
	}
}