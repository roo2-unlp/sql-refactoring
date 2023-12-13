import static org.junit.Assert.*;
import org.junit.Test;

public class RefactoringOrderByTest {

	
	  @Test
	    public void testTransformAddOrderBy() throws RefactoringException{
	        Refactoring refactoring = new RefactoringOrderBy();
	        String query = "SELECT NOMBRE FROM PERSONA P";
	        // Verificar despues esto.
	        ((RefactoringOrderBy) refactoring).setParameter("NOMBRE");
	        String result = refactoring.refactor(query);
	        assertEquals(result,"SELECT NOMBRE FROM PERSONA P Order By NOMBRE");
	    }
	  
	  @Test // CON GROUP BY
	    public void testTransformOrderByWithGroupBy() throws RefactoringException {
	        Refactoring refactoring = new RefactoringOrderBy();
	        String query = "SELECT NOMBRE, EDAD, COUNT(*) FROM PERSONA P GROUP BY EDAD";
	        ((RefactoringOrderBy) refactoring).setParameter("EDAD, NOMBRE");
	        String result = refactoring.refactor(query);
	        assertEquals(result, "SELECT NOMBRE, EDAD, COUNT(*) FROM PERSONA P GROUP BY EDAD Order By EDAD, NOMBRE"); 
	    }
	  
	  
	  @Test 
	  public void testOrderByInUnionQuery() throws RefactoringException {
	    	Refactoring refactoring = new RefactoringOrderBy();
	    	String query = "SELECT NOMBRE FROM tabla1 UNION SELECT NOMBRE FROM tabla2";
	    	((RefactoringOrderBy) refactoring).setParameter("NOMBRE");
	    	String result = refactoring.refactor(query);
	    	assertEquals(result, "SELECT NOMBRE FROM tabla1 UNION SELECT NOMBRE FROM tabla2 Order By NOMBRE"); 
	    } 
	  
	  
	  @Test // Campo Vacio
	    public void testFielEmpty() {
	        Refactoring refactoring = new RefactoringOrderBy();
	        String query = "SELECT NOMBRE FROM PERSONA P";
	        ((RefactoringOrderBy) refactoring).setParameter("");
	        assertThrows(RefactoringException.class, () -> {
	            String result = refactoring.refactor(query);
	        });
	    }
	  
	  @Test // Error sintaxis , Frm en vez de From
	    public void testSintaxisError() {
	        Refactoring refactoring = new RefactoringOrderBy();
	        String query = "SELECT NOMBRE FRM PERSONA P";
	        assertThrows(RefactoringException.class, () -> {
	            String result = refactoring.refactor(query);
	        });
	    }
	  
	  @Test // CON ORDER BY
	    public void testTransformNoOrderByWhenExists() {
	        Refactoring refactoring = new RefactoringOrderBy();
	        String query = "SELECT NOMBRE FROM PERSONA P ORDER BY NOMBRE";
	        assertThrows(RefactoringException.class, () -> {
	            String result = refactoring.refactor(query);
	        });
	    }
	  
	  @Test // CON DELETE
	    public void testNoOrderByInDeleteQuery() {
	        Refactoring refactoring = new RefactoringOrderBy();
	        String query = "DELETE FROM tabla WHERE condicion = 'condicionX'";
	        assertThrows(RefactoringException.class, () -> {
	            String result = refactoring.refactor(query);
	        });
	    }

	    @Test // CON CREATE
	    public void testNoOrderByInCreateTableQuery() {
	        Refactoring refactoring = new RefactoringOrderBy();
	        String query = "CREATE TABLE nueva_tabla (columna1 TEXT, columna2 INTEGER)";
	        assertThrows(RefactoringException.class, () -> {
	            String result = refactoring.refactor(query);
	        });
	    }

	    @Test // CON INSERT
	    public void testNoOrderByInInsertQuery() {
	        Refactoring refactoring = new RefactoringOrderBy();
	        String query = "INSERT INTO tabla (columna1, columna2) VALUES ('valorX', 'valorY')";
	        assertThrows(RefactoringException.class, () -> {
	            String result = refactoring.refactor(query);
	        });
	    }
	    
	    
	    @Test // No es una consulta SQLite.
	    public void testNoSQLite() {
	        Refactoring refactoring = new RefactoringOrderBy();
	        String query = "Orientacion objetos 2";
	        assertThrows(RefactoringException.class, () -> {
	            String result = refactoring.refactor(query);
	        });
	    }

	
}
