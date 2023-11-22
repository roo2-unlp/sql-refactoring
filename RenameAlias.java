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
			this.newAlias = newAlias;
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
		
		CheckPreconditionsVisitor visitor = new CheckPreconditionsVisitor(this.alias, this.newAlias);
        String checked_query = visitor.visit(newParseTree);
        
        if (checked_query.equals(text) && (parser.getNumberOfSyntaxErrors() == 0)) {
        	// Guardo el texto de la consulta
    		preconditionText = newParseTree.getText();
        	return true;
        }
		preconditionText = null;
		return false;
	}

	@Override
	protected String transform(String text) {
        SQLiteParser parser = this.createSQLiteParser(text);
        ParseTree tree = parser.parse();

		TransformAliasVisitor visitor = new TransformAliasVisitor();
        String transformedText = visitor.visit(tree);
        
        return transformedText;
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
		else {
        	//return !(preconditionText.equals(text));
			// && metodo que chequea que el alias haya sido modificado en todos los lugares;
			if (!(preconditionText.equals(this.transform(text)))){
				return true;
			}
		}	
			
	}
}

