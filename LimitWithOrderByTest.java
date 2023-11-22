import static org.junit.Assert.*;
import java.beans.Transient;
import org.junit.Test;

public class LimitWithOrderByTest {
    
    @Test 
    public void limitwithorderbyrefactorAQuery() throws RefactoringException {
        String result;
        Refactoring refactoring = new LimitWithOrderBy();
        result = refactoring.refactor("SELECT * FROM table_name;");  
        assertTrue(result.length() == 27);
    }

    // deberia dar error de sintaxis
    @Test 
    public void limitwithorderbyrefactorABadQuery()  { 
        boolean failure = false;
        Refactoring refactoring = new LimitWithOrderBy();
        try{
            refactoring.refactor("FOOBAR * WHERE 1=1;");  
        }
        catch(Exception e) { failure = true; }
        assertTrue(failure);
    } 

    @Test
    public void testLimitWithOrderByRefactor() throws RefactoringException {
        LimitWithOrderBy refactoring = new LimitWithOrderBy();
        
        // Caso de prueba: Consulta válida con ORDER BY
        String inputQuery = "SELECT * FROM table_name ORDER BY column_name;";
        assertTrue(refactoring.checkPreconditions(inputQuery));
        
        String result = refactoring.transform(inputQuery);
        assertEquals("SELECT * FROM table_name ORDER BY column_name LIMIT 10", result);
        
        assertTrue(refactoring.checkPostconditions(result));
    }

    @Test
    public void testLimitWithOrderByInvalidQuery() throws RefactoringException {
        LimitWithOrderBy refactoring = new LimitWithOrderBy();
        
        // Caso de prueba: Consulta inválida sin ORDER BY
        String inputQuery = "SELECT * FROM table_name;";
        assertFalse(refactoring.checkPreconditions(inputQuery));
        
        // La transformación no debería cambiar la consulta
        String result = refactoring.transform(inputQuery);
        assertEquals(inputQuery, result);
        
        // No se deben cumplir las postcondiciones
        assertFalse(refactoring.checkPostconditions(result));
    }
}
    

// agregar test para verificar que hacemos si hay una subquery? 

/* test a revisar - agregar otros si es necesario 

    // Verifica que la consulta refactorizada tenga una cláusula LIMIT pero no ORDER BY
    @Test
    public void limitWithOrderByValidQueryWithOrderBy() throws RefactoringException {
        Refactoring refactoring = new LimitWithOrderBy();
        String inputQuery = "SELECT * FROM table_name ORDER BY column_name;";
        //si no tiene limit hay que agregarlo. llama refactor
        String result = refactoring.refactor(inputQuery);
        assertEquals("SELECT * FROM table_name LIMIT 5;", result);
    }


    // Verificar que la columna utilizada en ORDER BY existe en la tabla   

    // Verifica si existe o no columna de ordenamiento 
    @Test
    public void limitWithOrderByValidQueryWithOrderByNoColumn() {
        Refactoring refactoring = new LimitWithOrderBy();
        String inputQuery = "SELECT * FROM table_name ORDER BY LIMIT 5;"; 
        try {
            refactoring.refactor(inputQuery);
            assertFalse("RefactoringException not thrown for missing ORDER BY column.", true);
        } catch (RefactoringException e) {
            assertTrue(true);
        }
    }
*/