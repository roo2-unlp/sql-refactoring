package sqlitegrammar;

public class CheckPreVisitrSelect extends SQLiteParserBaseVisitor<Void>{

    private int cantSelect = 0;
       
    @Override
     public Void visitSql_stmt(SQLiteParser.Sql_stmtContext ctx) {
           
            if(ctx.select_stmt() != null) {
                this.cantSelect += 1;
            }
        
        return super.visitSql_stmt(ctx);
     }
    
    public int cantSelect() {
        return cantSelect;
    }
}
