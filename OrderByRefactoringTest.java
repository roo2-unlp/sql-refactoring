import static org.junit.Assert.*;
import java.beans.Transient;
import org.junit.Test;

public class OrderByRefactoringTest {

    @Test
    public void testTransformAddOrderBy() throws RefactoringException{
        Refactoring refactoring = new RefactoringOrderBy();
        String query = "SELECT P.NOMBRE FROM PERSONA P";
        String result = refactoring.refactor(query);
        assertEquals(result,"SELECT P.NOMBRE FROM PERSONA P ORDER BY P.NOMBRE");
    }

	@Test // ESTE TEST DEBERIA AGREGAR EL ORDER BY AL FINAL DEL QUERY + NOMBREDECOLUMNA.
		  //EJ:"(SELECT columna1 FROM tabla1) UNION (SELECT columna2 FROM tabla2) ORDER BY columna1"
	public void testOrderByInUnionQuery() throws RefactoringException {
		Refactoring refactoring = new RefactoringOrderBy();
		String query = "(SELECT columna1 FROM tabla1) UNION (SELECT columna2 FROM tabla2)";
		String result = refactoring.refactor(query);
		assertEquals(result, query); 
	}



    //Test para verificar que tipo de consulta es, si la consulta es distinta a SELECT ... -> no hace nada
    @Test
    public void testTransformNoOrderByWhenInsert() {
  		 String sentenciaSQL = "Insert into Persona(nombre,edad,dni);";
  		 Refactoring refactoring = new RefactoringOrderBy();
   		try {
      			String sentenciaTransformada = refactoring.refactor(sentenciaSQL);
      			assertTrue(sentenciaTransformada.contains("Order By"));
  		 } catch (RefactoringException e) {
    		  	System.out.println(e.getMessage() + "No se puedo transformar sentencia. Sentencia Final: " + sentenciaSQL);
      		    
   		}
	}

	@Test // CON ORDER BY
    public void testTransformNoOrderByWhenExists() {
        Refactoring refactoring = new RefactoringOrderBy();
        String query = "SELECT P.NOMBRE FROM PERSONA P ORDER BY P.NOMBRE";
        assertThrows(RefactoringException.class, () -> {
            String result = refactoring.refactor(query);
        });
    }

    @Test // CON GROUP BY
    public void testTransformNoOrderByWithGroupBy() {
        Refactoring refactoring = new RefactoringOrderBy();
        String query = "SELECT P.NOMBRE, COUNT(*) FROM PERSONA P GROUP BY P.NOMBRE";
        assertThrows(RefactoringException.class, () -> {
            String result = refactoring.refactor(query);
        });
    }

    @Test // CON UPDATE
    public void testNoOrderByInUpdateQuery() {
        Refactoring refactoring = new RefactoringOrderBy();
        String query = "UPDATE tabla SET columna1 = 'valorX' WHERE condicion = 'condicionX'";
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
}