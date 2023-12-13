import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.CharStreams;

public class RefactoringOrderBy extends Refactoring {

	private String nombreCampo = "";
		
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
		
		//Verificar que campo sea != ""
		if (nombreCampo.equals("")) {
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
	}
	
	
	
	
	public String transform(String text) {
		SQLiteParser parser = this.createSQLiteParser(text);
	    ParseTree newParseTree = parser.parse();

		String transformedQuery = text + " Order By " + nombreCampo;
		return transformedQuery; 
	}

	public boolean checkPostconditions(String text) {
		SQLiteParser parser = this.createSQLiteParser(text);
	    ParseTree newParseTree = parser.parse();
		
		//Chequea que no haya error de sintaxis
		if (parser.getNumberOfSyntaxErrors() > 0) {
			return false;
		}
		
		VisitorExistsOrderBy visitor = new VisitorExistsOrderBy();
		visitor.visit(newParseTree);
		
		//Verificamos que se haya hecho la transformacion de agregar order by.
		return visitor.isValid();
	}
	
	
	public void setParameter(String nombreCampo) {
		this.nombreCampo = nombreCampo;
	}
	
	
}
