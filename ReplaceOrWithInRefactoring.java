import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import sqlitegrammar.*;
public class ReplaceOrWithInRefactoring extends Refactoring{
    private String preconditionText = null;

	private SQLiteParser createSQLiteParser (String text) {
        CharStream charStream = CharStreams.fromString(text);
        SQLiteLexer lexer = new SQLiteLexer(charStream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        return new SQLiteParser(tokens);
    }

	@Override
	protected boolean checkPreconditions(String text) {
        SQLiteParser parser = this.createSQLiteParser(text);
        ParseTree newParseTree = parser.parse();

        if (parser.getNumberOfSyntaxErrors() > 0 || 
        (!this.chequearOR(text)  || !this.chequearComparacionIgualdad(text) || !this.chequearIgualdadDeCampos(text) 
        || !this.chequearCondiciones(text))) {
            preconditionText = null;
            return false;
        }

        preconditionText = newParseTree.getText();
        
        return true;
    }

    //SELECT * FROM empleados WHERE estado_civil = 'Soltero' OR estado = 'Casado';

    private String obtenerClausulaWhere(String text) {
        String[] partesSql = text.split("WHERE");
        if (partesSql.lenght() > 2 ){
            return throw new Exception;
        }
        return partesSql[1]; //Me quedo con la clausula where y su contenido
    }

    private boolean chequearOR(String text) {
        return obtenerClausulaWhere(text).contains(" OR ");
    }

    private boolean chequearComparacionIgualdad(String text) {
        String cadena = obtenerClausulaWhere(text);
        return cadena.contains("=") && !contieneOperadoresComparacion(cadena);
    }

    private boolean contieneOperadoresComparacion(String cadena) {
        return cadena.contains(">") || cadena.contains(">=") || 
            cadena.contains("<") || cadena.contains("<=") ||
            cadena.contains("!=");
    }

    private boolean chequearIgualdadDeCampos(String text) {
        String cadena = obtenerClausulaWhere(text);
        String condicionOR = cadena.split("OR");
        String[] campo = condicionOR.split("=");
        for (String condicion : condicionOR) {
            if (!condicion.trim().startsWith(campo[0])) {
                return false;
            }
        }
        return true;
    }

    private boolean chequearCondiciones(String text) {
    String[] condiciones = text.split(" OR ");

    for (String condicion : condiciones) {
        if (!condicion.trim().startsWith("estado_civil = ")) {
            return false;
        }
    }

    return true;
}
	@Override
	protected String transform(String text) {
		// TODO Auto-generated method stub
		return this.checkPreconditions(text);
	}

	@Override
	protected boolean checkPostconditions(String text) {
		// TODO Auto-generated method stub
		return false;
	}

}
