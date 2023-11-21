
public class RefactoringOrderBy extends Refactoring {

		
	private SQLiteParser createSQLiteParser(String text) {
        CharStream charStream = CharStreams.fromString(text);
        SQLiteLexer lexer = new SQLiteLexer(charStream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        return new SQLiteParser(tokens);
    }
	
	
	public boolean checkPreconditions(String text) {
		SQLiteParser parser = this.createSQLiteParser(text);
	    ParseTree newParseTree = parser.parse();
		
		//Chequea que no haya error de sintaxis
		if (parser.getNumberOfSyntaxErrors() > 0) {
			return false;
		}
		
		//Este visitor se fija si la sentencia tiene o no ya el order By.
		//en caso de que ya lo tengo no tengo que hacer nada.
		
		VisitorExistsOrderBy visitor = new VisitorExistsOrderBy();
		visitor.visit(newParseTree);
		
		return !visitor.isValid();
		
	}
	
	public String transform(String text) {
		
	}

	public boolean checkPostconditions(String text) {
	
	}
	
	
}
