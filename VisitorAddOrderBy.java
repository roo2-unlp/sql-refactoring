
public class VisitorAddOrderBy extends SQLiteParserBaseVisitor<Void>{

	private StringBuilder textTransformar = new StringBuilder();
	private String columnaOrderby;
	
	//Metodo para obtener primer campo de la sentencia SELECT
	@Override
    public Void visitResult_column(SQLiteParser.Result_columnContext ctx) {
        String nombreColumna = ctx.getText();      
        columnaOrderby = nombreColumna;       
        return null;
    }
	
	 @Override
	 public String visitOrdering_term(SQLiteParser.Ordering_termContext ctx) {
	     // Agregar Order By con la primer columna de la consulta. 
		 // Tengo que chequear si esta el order by otra vez?
		 // O simplemente sacar este metodo. Chequear Despues
		 textTransformar.append(" ORDER BY ").append(columnaOrderby);
	     return null;
	 }
	
	
}
