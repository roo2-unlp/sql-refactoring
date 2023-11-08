
public class RenameAlias extends Refactoring {
	// alias que se quiere cambiar
	private String alias;
	// nuevo alias
	// private String newAlias;

	public void renameAlias(String alias) {
		this.alias = alias;
	}

	/*
	 * public void setAlias(String alias) {
	 * this.alias = alias;
	 * }
	 * 
	 * public void setNewAlias(String newAlias) {
	 * this.newAlias = newAlias;
	 * }
	 */
	public boolean aliasExist() {
		return true;
	}

	public void newAliasNotExist() {

	}

	@Override
	protected boolean checkPreconditions(String text) {
		// Que la consulta tenga un alias

		// Que no exista otro alias igual al que quiero cambiar
		// No utilizar palabras reservadas
		// Que el cambio no genere conflictos en la subquery
		// Que el nuevo alias no exista

		// Que el nuevo alias no sea igual al nombre de la tabla o de otra columna
		return false;
	}

	@Override
	protected String transform(String text) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected boolean checkPostconditions(String text) {
		// Que el alias haya sido modificado en todos los lugares y no esté el anterior
		return false;
	}

}
