import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.Before;


public class RenameAliasTest {
	private RenameAlias refactoring;
	private String queryWithAlias;
	private String queryWithoutAlias;
	private String queryRefactored;

	@Before
	void setUp() throws Exception {

		refactoring = new RenameAlias();
		// Seteo el alias que quiero cambiar y el nuevo alias
		refactoring.setAlias("nom_pais", "pais");

		queryWithAlias = "SELECT nombre AS nom_pais FROM paises";
		// Query sin alias
		queryWithoutAlias = "SELECT nombre FROM paises";
		// Query refactorizada
		queryRefactored = "SELECT nombre AS pais FROM paises";
	}

	@Test
	public void testAliasExist() {
		// Testea que si el alias existe, se haya cambiado correctamente
		assertTrue(refactoring.aliasExist(queryWithAlias, "nom_pais"));
		try {
			assertEquals(queryRefactored, refactoring.refactor(queryWithAlias));
		} catch (RefactoringException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testAliasNotExist() {
		// Testea que si el alias no existe la query no haya cambiado
		assertFalse(refactoring.aliasExist(queryWithAlias, "nom_p"));
		try {
			assertEquals(queryWithAlias, refactoring.refactor(queryWithAlias));
		} catch (RefactoringException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testQueryWithouAlias() {
		// Testea que la query sin alias no se haya cambiado
		try {
			assertEquals(queryWithoutAlias, refactoring.refactor(queryWithoutAlias));
		} catch (RefactoringException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testNewAliasNotExist() {
		// Testea que el nuevo alias no exista
		assertTrue(refactoring.newAliasNotExist(queryWithAlias, "pais"));
	}

	@Test
	public void testNewAliasExist() {
		// Testea que si el nuevo alias ya existe no se hacen cambios
		try {
			assertEquals(queryRefactored, refactoring.refactor(queryRefactored));
		} catch (RefactoringException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testInvalidAlias() {
		// Testea que el alias no sea una palabra reservada
		refactoring.setAlias("nom_pais", "*");
		try {
			assertEquals(queryWithAlias, refactoring.refactor(queryWithAlias));
		} catch (RefactoringException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testAliasIsNameOfTable() {
		// Testea que si el alias es igual al nombre de la tabla no se hace el cambio
		refactoring.setAlias("nom_pais", "paises");
		try {
			assertEquals(queryWithAlias, refactoring.refactor(queryWithAlias));
		} catch (RefactoringException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testAliasIsNameOfColumn() {
		// Testea que si el alias es igual al nombre de la columna no se hace el cambio
		refactoring.setAlias("nom_pais", "nombre");
		try {
			assertEquals(queryWithAlias, refactoring.refactor(queryWithAlias));
		} catch (RefactoringException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}