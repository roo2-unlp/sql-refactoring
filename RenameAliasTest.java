
public class RenameAliasTest {

	/*@Test
	public void renameAlias() {
		String result;
		Refactoring refactoring = new RenameAlias();
		String query = "FROM Products AS Pro\r\n"
				+ "	INNER JOIN Categories AS Cat\r\n"
				+ "	ON Pro.CategoryID = Cat.CategoryID";
		assertNotEqual(nombreAlias, refactoring.refactor(query))
		refactoring.setAlias(nombreAlias);
		assertEquals()
	}
	*/
	@Test
    public void testRenameAlias() throws RefactoringException {
		Refactoring refactoring = new RenameAlias("pais");

        String query = "SELECT nombre AS nom_pais FROM paises";

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
		Refactoring refactoring = new RenameAlias("pais");
		String query = "SELECT nombre AS pais FROM paises";
		
		try {
			refactoring.refactor(query);
			fail("se deberia lanzar una excepción porque el alias ya existe")
		} catch (RefactoringException e) {
			assertEquals("Preconditions not met.", e.getMessage());
		}
	}
	
	//falta terminar 
	@Test
	public void newAliasNotExist() {
		Refactoring refactoring = new RenameAlias("pais");
		String query = "SELECT nombre AS pais FROM paises";
		
		refactoring.refactor(query);
		
		try {
			refactoring.refactor(query);
			fail("se deberia lanzar una excepción porque el alias ya existe")
		} catch (RefactoringException e) {
			assertEquals("Preconditions not met.", e.getMessage());
		}
	}
	
	
}
