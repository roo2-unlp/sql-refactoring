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
				+ " FROM ciudades"
				+ " WHERE nom_pais IN ("
				+ " SELECT nombre_pais"
				+ " FROM paises"
				+ " WHERE continente = 'América'"
				+ ")"
				+ ") AS cities"
				+ " JOIN paises AS p ON cities.nombre_pais = p.nombre_pais;";

		// Query without alias
		queryWithoutAlias = "SELECT nombre_ciudad, ciudades.nombre_pais, paises.continente"
				+ " FROM ("
				+ " SELECT nombre_ciudad, nombre_pais"
				+ " FROM ciudades"
				+ " WHERE nombre_pais IN ("
				+ " SELECT nombre_pais"
				+ " FROM paises"
				+ " WHERE continente = 'América'"
				+ ")"
				+ ") ciudades"
				+ " JOIN paises ON ciudades.nombre_pais = paises.nombre_pais;";

		// Query refactored
		queryColumnRefactored = "SELECT nombre_ciudad, cities.nombre_pais, p.continente"
				+ " FROM ("
				+ " SELECT nombre_ciudad, nombre_pais AS pais"
				+ " FROM ciudades"
				+ " WHERE pais IN ("
				+ " SELECT nombre_pais"
				+ " FROM paises"
				+ " WHERE continente = 'América'"
				+ ")"
				+ ") AS cities"
				+ " JOIN paises AS p ON cities.nombre_pais = p.nombre_pais;";

		// Query refactored
		queryTableRefactored = "SELECT nombre_ciudad, c.nombre_pais, p.continente"
				+ " FROM ("
				+ " SELECT nombre_ciudad, nombre_pais AS nom_pais"
				+ " FROM ciudades"
				+ " WHERE nom_pais IN ("
				+ " SELECT nombre_pais"
				+ " FROM paises"
				+ " WHERE continente = 'América'"
				+ ")"
				+ ") AS c"
				+ " JOIN paises AS p ON c.nombre_pais = p.nombre_pais;";

	}

	@Test
	public void testAliasExist() {
		// Testea que si el alias existe, se haya cambiado correctamente
		// rename alias de una columna
		try {
			refactoring.setAlias("nom_pais", "pais");
			assertEquals(queryColumnRefactored.replaceAll("\\s", ""), 
			        refactoring.refactor(queryWithAlias).replaceAll("\\s", ""));			// rename alias de una tabla
			refactoring.setAlias("cities", "c");
			assertEquals(queryTableRefactored.replaceAll("\\s", ""), 
			        refactoring.refactor(queryWithAlias).replaceAll("\\s", ""));
			System.out.println("El refacoting se realizó con exito");
		} catch (RefactoringException e) {
			System.out.println("Falló EL testAliasExist");
			assertTrue(false);
		}
	}

	@Test
	public void testAliasNotExist() {
		// Testea que si el alias no existe la query no haya cambiado
		try {
			// rename alias de una columna
			refactoring.setAlias("nom_p", "pais");

			assertEquals(queryWithAlias, refactoring.refactor(queryWithAlias));
			// rename alias de una tabla
			refactoring.setAlias("ciu", "c");
			assertEquals(queryWithAlias, refactoring.refactor(queryWithAlias));
		} catch (RefactoringException e) {
			System.out.println("falló EL testAliasNotExist");
			assertTrue(false);
			//e.printStackTrace();
		}
	}

	@Test
	public void testQueryWithoutAlias() {
		// Testea que la query sin alias no se haya cambiado
		try {
			// rename alias de una columna
			refactoring.setAlias("nom_p", "pais");
			assertEquals(queryWithoutAlias, refactoring.refactor(queryWithoutAlias));
			// rename alias de una tabla
			refactoring.setAlias("ciudades", "c");
			assertEquals(queryWithoutAlias, refactoring.refactor(queryWithoutAlias));
		} catch (RefactoringException e) {
			System.out.println("falló EL testQueryWithoutAlias");
			assertTrue(false);
			//e.printStackTrace();
		}
	}

	@Test
	public void testNewAliasNotExist() {
		// Testea que el nuevo alias no exista
		try {
			// rename alias de una columna
			refactoring.setAlias("nom_pais", "pais");
			assertEquals(queryWithAlias, refactoring.refactor(queryWithAlias));
			// rename alias de una tabla
			refactoring.setAlias("cities", "c");
			assertEquals(queryWithAlias, refactoring.refactor(queryWithAlias));
		} catch (RefactoringException e) {
			System.out.println("falló EL testNewAliasNotExist");
			assertTrue(false);
			//e.printStackTrace();
		}
	}

	@Test
	public void testNewAliasExist() {
		// Testea que si el nuevo alias ya existe no se hacen cambios
		try {
			// rename alias de una columna
			refactoring.setAlias("nom_pais", "p");
			assertEquals(queryWithAlias, refactoring.refactor(queryWithAlias));
			// rename alias de una tabla
			refactoring.setAlias("cities", "c");
			assertEquals(queryWithAlias, refactoring.refactor(queryWithAlias));
		} catch (RefactoringException e) {
			System.out.println("falló EL testNewAliasExist");
			assertTrue(false);
			//e.printStackTrace();
		}
	}

	@Test
	public void testInvalidAlias() {
		// Testea que el alias no sea una palabra reservada
		refactoring.setAlias("nom_pais", "*");
		try {
			assertEquals(queryWithAlias, refactoring.refactor(queryWithAlias));
		} catch (RefactoringException e) {
			System.out.println("falló EL testInvalidAlias");
			assertTrue(false);
			//e.printStackTrace();
		}
	}

	@Test
	public void testAliasIsNameOfTable() {
		// Testea que si el alias es igual al nombre de la tabla no se hace el cambio
		refactoring.setAlias("p", "paises");
		assertThrows(RefactoringException.class, () -> refactoring.refactor(queryWithAlias));
	}

	@Test
	public void testAliasIsNameOfColumn() {
		// Testea que si el alias es igual al nombre de la columna no se hace el cambio
		refactoring.setAlias("nom_pais", "nombre_pais");
		try {
			assertEquals(queryWithAlias, refactoring.refactor(queryWithAlias));
		} catch (RefactoringException e) {
			System.out.println("falló EL testAliasIsNameOfColumn");
			assertTrue(false);
			//e.printStackTrace();
		}
	} 
}
