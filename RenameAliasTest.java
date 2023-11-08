import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

public class RenameAliasTest {

	/*
	 * @Test
	 * public void renameAlias() {
	 * String result;
	 * Refactoring refactoring = new RenameAlias();
	 * String query = "FROM Products AS Pro\r\n"
	 * + "	INNER JOIN Categories AS Cat\r\n"
	 * + "	ON Pro.CategoryID = Cat.CategoryID";
	 * assertNotEqual(nombreAlias, refactoring.refactor(query))
	 * refactoring.setAlias(nombreAlias);
	 * assertEquals()
	 * }
	 */
	@Test
	public void testRenameAlias() throws RefactoringException {
		RenameAlias refactoring = new RenameAlias();
		refactoring.renameAlias("pais"); // agregaría también nuevo alias
		String query = "SELECT nombre AS nom_pais FROM paises";

		// chequearía las precondiciones antes de refactorizar
		String refactoredSql = refactoring.refactor(query);

		boolean preconditions = refactoring.checkPreconditions(query);
		boolean postconditions = refactoring.checkPostconditions(refactoring.refactor(query));

		// Verifica que las condiciones previas y posteriores se cumplan
		assertTrue(preconditions);
		assertTrue(postconditions);

		assertEquals("SELECT nombre AS pais FROM paises", refactoredSql);
	}

	@Test
	public void aliasExist() {
		RenameAlias refactoring = new RenameAlias();
		// chequear que el alias que quiero cambiar exista
		refactoring.renameAlias("pais");
		String query = "SELECT nombre AS pais FROM paises";

		try {
			refactoring.refactor(query);
			fail("se deberia lanzar una excepción porque el alias ya existe");
		} catch (RefactoringException e) {
			assertEquals("Preconditions not met.", e.getMessage());
		}
	}

	@Test
	public void newAliasNotExist() {
		RenameAlias refactoring = new RenameAlias();
		// chequear que el nuevo alias no exista
		refactoring.renameAlias("pais");
		String query = "SELECT nombre AS p FROM paises";
		String refactoredSql = refactoring.refactor(query);
		assertEquals("SELECT nombre AS pais FROM paises", refactoredSql);
	}

	@Test
	public void postConditionsFailed() {
		RenameAlias refactoring = new RenameAlias();
		refactoring.renameAlias("legajo* ");
		String query = "SELECT numero_de_legajo AS nro_legajo FROM estudiantes";
		try {
			String refactoredSql = refactoring.refactor(query);
			fail("se deberia lanzar una excepción porque el alias es válido pero"
					+ "la consulta es sintacticamente incorrecta");
		} catch (RefactoringException e) {
			assertEquals("Postconditions not met.", e.getMessage());
		}
	}
}
