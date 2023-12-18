import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

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

		if (this.esAliasValido(this.alias) && this.esAliasValido(this.newAlias) &&
				(parser.getNumberOfSyntaxErrors() == 0) && (visitor.getEsValido())) {
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
		SeparateTokensVisitor tokensVisitor = new SeparateTokensVisitor();
		tokensVisitor.visit(newTree);
		
		transformedText = tokensVisitor.getSeparatedWords().toString();
		return transformedText;
	}

	@Override
	protected boolean checkPostconditions(String text) {
		SQLiteParser parser = this.createSQLiteParser(text);
		ParseTree tree = parser.parse();
		
		CheckAliasVisitor visitor = new CheckAliasVisitor(this.newAlias, this.alias);
		String checkedQuery = visitor.visit(tree);
		
		if (visitor.getEsValido()) {
			ParseTree newTree = tree;
			SeparateTokensVisitor tokensVisitor = new SeparateTokensVisitor();
			tokensVisitor.visit(newTree);
			
			if (transformedText.equals(tokensVisitor.getSeparatedWords().toString())) {
				return true;
			}
		}
		return false;
	}

	private boolean esPalabraReservada(String newAlias) {
		boolean reservada = true;

		List<String> palabrasReservadas = new ArrayList<String>(Arrays.asList("SELECT", "FROM", "WHERE", "AS",
				"JOIN", "ON", "AND", "OR", "NOT", "IN", "LIKE",
				"BETWEEN", "IS", "NULL", "ORDER", "BY", "GROUP", "HAVING", "UNION", "ALL",
				"INTERSECT", "EXCEPT", "MINUS", "ANY", "SOME", "EXISTS", "CASE", "WHEN", "THEN",
				"ELSE", "END", "CREATE", "TABLE", "PRIMARY", "KEY", "*"));
		
		// Convertir cada palabra a mayúsculas y verificar si es una palabra reservada
			newAlias = newAlias.toUpperCase();
			if (!palabrasReservadas.contains(newAlias))
				reservada = false;
		return reservada;
	}

	private boolean esAliasValido(String alias) {
		if ((alias.length() >= 1) && (alias.matches("[a-zA-Z_]+")) && !this.esPalabraReservada(alias)) {
			return true;
		}
		return false;
	}
}