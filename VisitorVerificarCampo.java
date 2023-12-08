
public class VisitorVerificarCampo extends SQLiteParserBaseVisitor<Void>{

	private String nombreCampo;
	private boolean existe_nombre;
	
	public VerificarCampo (String nombreCampo) {
		this.nombreCampo = nombreCampo;
	}
	
	@Override
	public Void visitSelect_core(SQLiteParser.Select_coreContext ctx) {
	   // Busca en cada result_column para saber si esta o no el campo.
	    existe_nombre = ctx.result_column()
	    		.stream()
	            .anyMatch(resultColumn -> resultColumn.getText().equals(nombreCampo));

	    return super.visitSelect_core(ctx); 
	}
	
	 
	 public boolean existeNombre() {
		 return this.existe_nombre;
	 }
}
