import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class RenameAliasTest {
	private RenameAlias refactoring;
	private String queryWithAlias;
	private String queryWithoutAlias;
	private String queryColumnRefactored;
	private String queryTableRefactored;

	@Before
	public void setUp() throws Exception {

		refactoring = new RenameAlias();

		// query with alias
		queryWithAlias = "SELECT nombre_ciudad, cities.nombre_pais, p.continente"
				+ " FROM ("
				+ " SELECT nombre_ciudad, nombre_pais AS nom_pais"
				+ " FROM ciudades AS cdades"
				+ " WHERE nom_pais IN ("
				+ " SELECT nombre_pais"
				+ " FROM paises"
				+ " WHERE continente = 'América' and cdades.nombre_ciudad = 'La Plata'"
				+ ")"
				+ ") AS cities"
				+ " JOIN paises AS p ON cities.nombre_pais = p.nombre_pais;";

		// Query without alias
		queryWithoutAlias = "SELECT *"
				+ " FROM provincias"
				+ " WHERE nombre_pais = 'Argentina';";

		// Query refactored
		queryColumnRefactored = "SELECT nombre_ciudad, cities.nombre_pais, p.continente"
				+ " FROM ("
				+ " SELECT nombre_ciudad, nombre_pais AS pais"
				+ " FROM ciudades AS cdades"
				+ " WHERE pais IN ("
				+ " SELECT nombre_pais"
				+ " FROM paises"
				+ " WHERE continente = 'América' and cdades.nombre_ciudad = 'La Plata'"
				+ ")"
				+ ") AS cities"
				+ " JOIN paises AS p ON cities.nombre_pais = p.nombre_pais;";

		// Query refactored
		queryTableRefactored = "SELECT nombre_ciudad, cities.nombre_pais, p.continente"
				+ " FROM ("
				+ " SELECT nombre_ciudad, nombre_pais AS nom_pais"
				+ " FROM ciudades AS c"
				+ " WHERE nom_pais IN ("
				+ " SELECT nombre_pais"
				+ " FROM paises"
				+ " WHERE continente = 'América' and c.nombre_ciudad = 'La Plata'"
				+ ")"
				+ ") AS cities"
				+ " JOIN paises AS p ON cities.nombre_pais = p.nombre_pais;";
	}

	@Test
	public void testAliasExist() {
		// Testea que si el alias existe y el nuevo alias no está siendo utilizado, la consulta se 
		// haya modificado correctamente
		try {
			// rename alias de una columna
			refactoring.setAlias("nom_pais", "pais");
			assertEquals(queryColumnRefactored, refactoring.refactor(queryWithAlias)); 

			// rename alias de una tabla
			refactoring.setAlias("cdades", "c");
			assertEquals(queryTableRefactored, refactoring.refactor(queryWithAlias));
		} catch (RefactoringException e) {
			assertTrue(false);
		}
	}

	@Test
	public void testAliasNotExist() {
		// Testea que la consulta no cambie si el alias que se quiere modificar no existe
		try {
			// rename alias de una columna
			refactoring.setAlias("nom_p", "pais");
			assertEquals(queryWithAlias, refactoring.refactor(queryWithAlias));

			// rename alias de una tabla
			refactoring.setAlias("ciu", "c");
			assertEquals(queryWithAlias, refactoring.refactor(queryWithAlias));
		} catch (RefactoringException e) {
			assertTrue(false);
		}
	}

	@Test
	public void testQueryWithoutAlias() {
		// Testea que no se modifique la consulta si esta no tiene alias
		try {
			// rename alias de una columna
			refactoring.setAlias("nom_p", "pais");
			assertEquals(queryWithoutAlias, refactoring.refactor(queryWithoutAlias));

			// rename alias de una tabla
			refactoring.setAlias("cities", "c");
			assertEquals(queryWithoutAlias, refactoring.refactor(queryWithoutAlias));
		} catch (RefactoringException e) {
			assertTrue(false);
		}
	}

	@Test
	public void testNewAliasExist() {
		// Testea que si el nuevo alias ya existe no se realicen modificaciones

		// rename alias de una columna
		refactoring.setAlias("nom_pais", "p");
		assertThrows(RefactoringException.class, () -> refactoring.refactor(queryWithAlias));
		// rename alias de una tabla
		refactoring.setAlias("c", "cities");
		assertThrows(RefactoringException.class, () -> refactoring.refactor(queryWithAlias));
	}

	@Test
	public void testInvalidAlias() {
		// Testea que el alias no sea una palabra reservada
		refactoring.setAlias("nom_pais", "*");
		assertThrows(RefactoringException.class, () -> refactoring.refactor(queryWithAlias));
	}

	@Test
	public void testAliasIsNameOfTable() {
		// Testea que si el alias es igual al nombre de la tabla no se hagan modificaciones 
		refactoring.setAlias("p", "paises");
		assertThrows(RefactoringException.class, () -> refactoring.refactor(queryWithAlias));
	}

	@Test
	public void testAliasIsNameOfColumn() {
		// Testea que si el alias es igual al nombre de la columna no se hagan modificaciones 
		refactoring.setAlias("nom_pais", "nombre_pais");
		assertThrows(RefactoringException.class, () -> refactoring.refactor(queryWithAlias));
	}
}