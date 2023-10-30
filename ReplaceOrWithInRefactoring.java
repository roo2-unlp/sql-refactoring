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
        try {
            if (parser.getNumberOfSyntaxErrors() > 0 || 
            (!this.chequearOR(text)  || !this.chequearComparacionIgualdad(text) || !this.chequearIgualdadDeCampos(text) )) {
                System.out.println("dentro IF");
                preconditionText = null;
                return false;
            }
        } catch (Exception e) {
            System.out.println("No cumple con la precondicion");
        }
        

        preconditionText = newParseTree.getText();
        
        return true;
    }

    //SELECT * FROM empleados WHERE estado_civil = 'Soltero' OR estado = 'Casado';

    private String obtenerClausulaWhere(String text) throws Exception{
        String[] partesSql = text.split("WHERE");
        if (partesSql.length > 2 ){
            throw new Exception();
        }
        return partesSql[1]; //Me quedo con la clausula where y su contenido
    }

    private boolean chequearOR(String text) throws Exception{
        return obtenerClausulaWhere(text).contains(" OR ");
    }

    private boolean chequearComparacionIgualdad(String text) throws Exception{
        String cadena = obtenerClausulaWhere(text);
        return cadena.contains("=") && !contieneOperadoresComparacion(cadena);
    }

    private boolean contieneOperadoresComparacion(String cadena) {
        return cadena.contains(">") || cadena.contains(">=") || 
            cadena.contains("<") || cadena.contains("<=") ||
            cadena.contains("!=");
    }

    private boolean chequearIgualdadDeCampos(String text) throws Exception{
        String cadena = obtenerClausulaWhere(text);
        String[] condicionOR = cadena.split("OR");
        String[] campo = condicionOR[0].split("=");
        for (String condicion : condicionOR) {
            if (!condicion.trim().startsWith(campo[0].trim())) {
                return false;
            }
        }
        return true;
    }


	@Override
	protected String transform(String text) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected boolean checkPostconditions(String text) {
		// TODO Auto-generated method stub
		return false;
	}

}
