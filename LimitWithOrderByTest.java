import static org.junit.Assert.*;
import java.beans.Transient;
import org.junit.Test;

public class LimitWithOrderByTest {

    // Caso de prueba: Consulta válida con ORDER BY pero sin LIMIT
    @Test
    public void testLimitWithOrderByRefactor() throws RefactoringException {
        LimitWithOrderBy refactoring = new LimitWithOrderBy();
        String inputQuery = "SELECT * FROM partidos ORDER BY nombre;";        
        String result = refactoring.refactor(inputQuery);
        assertEquals("SELECT * FROM partidos ORDER BY nombre LIMIT 10", result);
    }

    // Caso de prueba: Consulta válida con ORDER BY pero con LIMIT
    @Test
    public void testLimitWithOrderByRefactor() throws RefactoringException {
        LimitWithOrderBy refactoring = new LimitWithOrderBy();
        String inputQuery = "SELECT * FROM partidos ORDER BY nombre LIMIT 10;";        
        String result = refactoring.refactor(inputQuery);
        assertEquals("SELECT * FROM partidos ORDER BY nombre LIMIT 10", result);
    }
    // Caso de prueba: Consulta inválida sin ORDER BY
    @Test
    public void testLimitWithOrderByInvalidQuery() throws RefactoringException {
        LimitWithOrderBy refactoring = new LimitWithOrderBy();    
        String inputQuery = "SELECT * FROM partidos;";
        String result = refactoring.refactor(inputQuery);
        assertEquals(inputQuery, result);
    }

    // setea el valor del limit
    @Test
    public void testTransformAddLimitWithOrderByAndCustomLimit() throws RefactoringException {
        LimitWithOrderBy refactoring = new LimitWithOrderBy();
        String inputQuery = "SELECT P.NOMBRE FROM PERSONA P ORDER BY P.NOMBRE";
        refactoring.setLimit(5);
        String result = refactoring.refactor(inputQuery); 
        assertEquals(result, "SELECT P.NOMBRE FROM PERSONA P ORDER BY P.NOMBRE LIMIT 5");
    }

    // Agrega limit a consutla
    @Test
    public void testLimitOrderByInUnionQuery() throws RefactoringException {
        LimitWithOrderBy refactoring = new LimitWithOrderBy();
        String inputQuery = "(SELECT columna1 FROM tabla1) UNION (SELECT columna2 FROM tabla2 ORDER BY columna1)";
        String result = refactoring.refactor(inputQuery);
        assertEquals(result, inputQuery+ " LIMIT 10");
    }

    // Verifica si existe o no columna de ordenamiento 
    @Test
    public void limitWithOrderByValidQueryWithOrderByNoColumn() {
        Refactoring refactoring = new LimitWithOrderBy();
        String inputQuery = "SELECT * FROM table_name ORDER BY not_column LIMIT 5;"; 
        try {
            refactoring.refactor(inputQuery);
            assertFalse("RefactoringException not thrown for missing ORDER BY column.", true);
        } catch (RefactoringException e) {
            assertTrue(true);
        }
    }
    
}
    // agregar test para verificar que hacemos si hay una subquery? 
