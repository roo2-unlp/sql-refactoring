import static org.junit.Assert.assertTrue;
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

    // Verifica que la consulta refactorizada tenga una cláusula LIMIT pero no ORDER BY
    @Test
    public void limitWithOrderByValidQueryWithOrderBy() throws RefactoringException {
        Refactoring refactoring = new LimitWithOrderBy();
        String inputQuery = "SELECT * FROM table_name ORDER BY column_name LIMIT 5;";
        String result = refactoring.refactor(inputQuery);
        assertFalse(result.contains("ORDER BY"));
        assertTrue(result.contains("LIMIT"));
    }


    // Verifica que la columna utilizada en ORDER BY existe en la tabla   
    @Test
    public void limitWithOrderByValidQueryWithOrderByColumnExists() throws RefactoringException {
        Refactoring refactoring = new LimitWithOrderBy();
        String inputQuery = "SELECT * FROM table_name ORDER BY existing_column LIMIT 10;";
        String result = refactoring.refactor(inputQuery);
        assertEquals("SELECT * FROM table_name LIMIT 10;", result);
    }

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
}
