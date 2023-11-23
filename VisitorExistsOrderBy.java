
public class VisitorExistsOrderBy extends SQLiteParserBaseVisitor<Void>{
	
	private boolean hasOrderBy = false;
	
	// Metodo para chequear si la consulta ya tiene el order by o no.
	public void visitSelect_stmt(SQLiteParser.Select_stmtContext ctx) {
		
			if (ctx.order_by_stmt() != null) {
	        	// La sentencia tiene el order BY
	            hasOrderBy = true; 
	        } 

	    //return super.visitSelect_stmt(ctx);
	}
	
	public boolean isValid() {
		return hasOrderBy;
	}
	
}
