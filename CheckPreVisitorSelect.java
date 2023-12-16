public class CheckPreVisitorSelect extends SQLiteParserBaseVisitor<Void>{
    private boolean isSelect= false;
		
	@Override
	public Void visitSql_stmt(SQLiteParser.Sql_stmtContext ctx) {
	    	
		if(ctx.select_stmt() != null) {
		 	isSelect = true;
		}
		 
		return super.visitSql_stmt(ctx);
	}
	 
	public boolean isSelect() {
		return isSelect;
	}
}
