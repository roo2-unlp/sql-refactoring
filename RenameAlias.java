import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;

import sqlitegrammar.*;

public class RenameAlias extends Refactoring {
	// alias que se quiere cambiar
	private String alias;
	// nuevo alias
	private String newAlias;
	// texto dl transform
	// se usa para comparar luego con las postcondiciones
	private String transformedText;

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

		CheckAliasVisitor visitor = new CheckAliasVisitor(this.alias, this.newAlias);
		String checkedQuery = visitor.visit(newParseTree);
		// System.out.println("Imprimiendo checked query:" + checkedQuery);

		if (this.esAliasValido(this.alias) && this.esAliasValido(this.newAlias) &&
				(parser.getNumberOfSyntaxErrors() == 0) && (visitor.getEsValido())) {
			// Guardo el texto de la consulta
			System.out.println("chepreconditions se está ejecutando el if");
			return true;
		}
		return false;
	}

	@Override
	protected String transform(String text) {
		SQLiteParser parser = this.createSQLiteParser(text);
		ParseTree tree = parser.parse();

		TransformAliasVisitor visitor = new TransformAliasVisitor(this.alias, this.newAlias);
		visitor.visit(tree);
		ParseTree newTree = tree;
		SeparateTokensVisitor visitor1 = new SeparateTokensVisitor();
		// visitor1.visit(tree);
		visitor1.visit(newTree);
		System.out.println("ARBOL VISITADO en string:  " + visitor1);
		transformedText = visitor1.getSeparatedWords().toString();
		return transformedText;
	}

	@Override
	protected boolean checkPostconditions(String text) {
		SQLiteParser parser = this.createSQLiteParser(text);
		ParseTree tree = parser.parse();
		CheckAliasVisitor visitor = new CheckAliasVisitor(this.newAlias, this.alias);
		String checkedQuery = visitor.visit(tree);
		System.out.println("checkpostconditions se está ejecutando");
		if (visitor.getEsValido()) {
			ParseTree newTree = tree;
			SeparateTokensVisitor tokensVisitor = new SeparateTokensVisitor();
			tokensVisitor.visit(newTree);
			System.out.println("TRANSFORMED TEXT: " + transformedText);
			System.out.println("TOKENS VISITOR: " + tokensVisitor.getSeparatedWords().toString());
			if (transformedText.equals(tokensVisitor.getSeparatedWords().toString())) {
				return true;
			}
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