
public class RenameAlias extends Refactoring {
	// alias que se quiere cambiar
	private String alias;
	// nuevo alias
	private String newAlias;

	public void setAlias(String alias, String newAlias) {
		this.alias = alias;
		if (!esPalabraReservada(newAlias)) {
			this.newAlias = newAlias;
		} else
			this.newAlias = alias;
	}

	private boolean esPalabraReservada(String newAlias) {
		boolean reservada = true;
		// Dividir el nuevo alias en palabras
		String[] palabras = newAlias.split("\\s+");

		// Convertir cada palabra a mayúsculas y verificar si es una palabra reservada
		for (int i = 0; i < palabras.length; i++) {
			palabras[i] = palabras[i].toUpperCase();
			switch (palabras[i]) {
				case "SELECT":
				case "FROM":
				case "WHERE":
				case "AS":
				case "JOIN":
				case "ON":
				case "AND":
				case "OR":
				case "NOT":
				case "IN":
				case "LIKE":
				case "BETWEEN":
				case "IS":
				case "NULL":
				case "ORDER":
				case "BY":
				case "GROUP":
				case "HAVING":
				case "UNION":
				case "ALL":
				case "INTERSECT":
				case "EXCEPT":
				case "MINUS":
				case "ANY":
				case "SOME":
				case "EXISTS":
				case "CASE":
				case "WHEN":
				case "THEN":
				case "ELSE":
				case "END":
				case "CREATE":
				case "TABLE":
				case "PRIMARY":
				case "KEY":
				case "*":
					reservada = true;
					break;
				default:
					reservada = false;
					break;
			}
		}
		return reservada;
	}

	public boolean aliasExist(String query, String alias) {
		return true;
	}

	public boolean newAliasNotExist(String query, String alias) {
		return true;
	}

	@Override
	protected boolean checkPreconditions(String text) {
		// Que la consulta tenga un alias
		// Que no exista otro alias igual al que quiero cambiar
		// No utilizar palabras reservadas
		// Que el cambio no genere conflictos en la subquery
		// Que el nuevo alias no exista
		// Que el nuevo alias no sea igual al nombre de la tabla o de otra columna
		return true;
	}

	@Override
	protected String transform(String text) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected boolean checkPostconditions(String text) {
		// Que el alias haya sido modificado en todos los lugares y no esté el anterior
		return true;
	}

}
