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
        assertEquals("SELECT * FROM partidos ORDER BY nombre LIMIT 10;", result);
    }

    // Caso de prueba: Consulta válida con ORDER BY pero con LIMIT
    @Test
    public void testLimitWithOrderByRefactorLimit() throws RefactoringException {
        LimitWithOrderBy refactoring = new LimitWithOrderBy();
        String inputQuery = "SELECT * FROM partidos ORDER BY nombre LIMIT 10;";        
        String result = refactoring.refactor(inputQuery);
        assertEquals("SELECT * FROM partidos ORDER BY nombre LIMIT 10;", result);
    }
    
    // Caso de prueba: Consulta inválida sin ORDER BY
    @Test
    public void testLimitWithOrderByInvalidQuery() throws RefactoringException{
        LimitWithOrderBy refactoring = new LimitWithOrderBy();    
        String inputQuery = "SELECT * FROM partidos;";
        try{
            refactoring.refactor(inputQuery);
        }
        catch (RefactoringException e){
            assertNotNull(e.getMessage());
        }
    }

    // setea el valor del limit
    @Test
    public void testTransformAddLimitWithOrderByAndCustomLimit() throws RefactoringException {
        LimitWithOrderBy refactoring = new LimitWithOrderBy();
        String inputQuery = "SELECT P.NOMBRE FROM PERSONA P ORDER BY P.NOMBRE";
        refactoring.setLimit(5);
        String result = refactoring.refactor(inputQuery); 
        assertEquals(result, "SELECT P.NOMBRE FROM PERSONA P ORDER BY P.NOMBRE LIMIT 5;");
    }
    
}
    // agregar test para verificar que hacemos si hay una subquery? 
