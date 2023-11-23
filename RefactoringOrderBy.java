
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
		
		VisitorIsSelect visitorSelect = new VisitorIsSelect();
		visitorSelect.visit(newParseTree);
		
		//Validar que sea una consulta de tipo Select,se valida antes para evitar problemas.
		if(!visitorSelect.isSelect()){
			return false;
		}
		
		//Este visitor se fija si la sentencia tiene o no ya el order By.
		VisitorExistsOrderBy visitor = new VisitorExistsOrderBy();
		visitor.visit(newParseTree);
		
		
		return !visitor.isValid();

	//	SQLiteParser.Ordering_termContext orderByContext = parser.ordering_term();
    //   if (orderByContext == null) {
    //        preconditionText = null;
    //        return false;
    //    }
	//	preconditionText = newParseTree.getText();
    //    return true;
		
	}
	
	
	
	
	
	public String transform(String text) {
		SQLiteParser parser = this.createSQLiteParser(text);
	    ParseTree newParseTree = parser.parse();
	    VisitorAddOrderBy visitorAddOrder = new VisitorAddOrderBy();
	  //visitorAddOrder.visit(newParseTree);

		//visitorAddOrder.visitResult_column(text)?
		//visitorAddOrder.visitResult_column(text)?

		//String transformedQuery = text + visitorAddOrder.getTextToAppend();
		//return transformedQuery; 
	}

	public boolean checkPostconditions(String text) {
		SQLiteParser parser = this.createSQLiteParser(text);
	    ParseTree newParseTree = parser.parse();
		
		//Chequea que no haya error de sintaxis
		if (parser.getNumberOfSyntaxErrors() > 0) {
			return false
		}
		
		VisitorExistsOrderBy visitor = new VisitorExistsOrderBy();
		visitor.visit(newParseTree);
		
		//Verificamos que se haya hecho la transformacion de agregar order by.
		return visitor.isValid();
	}
	
	
}
