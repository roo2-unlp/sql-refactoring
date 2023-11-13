import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RenameAliasTest {
	private RenameAlias refactoring;
	private String queryWithAlias;
	private String queryWithoutAlias;
	private String queryRefactored;

	@BeforeEach
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
		assertEquals(queryRefactored, refactoring.refactor(queryWithAlias));
	}

	@Test
	public void testAliasNotExist() {
		// Testea que si el alias no existe la query no haya cambiado
		assertFalse(refactoring.aliasExist(queryWithAlias, "nom_p"));
		assertEquals(queryWithAlias, refactoring.refactor(queryWithAlias));
	}

	@Test
	public void testQueryWithouAlias() {
		// Testea que la query sin alias no se haya cambiado
		assertEquals(queryWithoutAlias, refactoring.refactor(queryWithoutAlias));
	}

	@Test
	public void testNewAliasNotExist() {
		// Testea que el nuevo alias no exista
		assertTrue(refactoring.newAliasNotExist(queryWithAlias, "pais"));
	}

	@Test
	public void testNewAliasExist() {
		// Testea que si el nuevo alias ya existe no se hacen cambios
		assertEquals(queryRefactored, refactoring.refactor(queryRefactored));
	}

	@Test
	public void testInvalidAlias() {
		// Testea que el alias no sea una palabra reservada
		refactoring.setAlias("nom_pais", "*");
		assertEquals(queryWithAlias, refactoring.refactor(queryWithAlias));
	}

	@Test
	public void testAliasIsNameOfTable() {
		// Testea que si el alias es igual al nombre de la tabla no se hace el cambio
		refactoring.setAlias("nom_pais", "paises");
		assertEquals(queryWithAlias, refactoring.refactor(queryWithAlias));
	}

	@Test
	public void testAliasIsNameOfColumn() {
		// Testea que si el alias es igual al nombre de la columna no se hace el cambio
		refactoring.setAlias("nom_pais", "nombre");
		assertEquals(queryWithAlias, refactoring.refactor(queryWithAlias));
	}
}