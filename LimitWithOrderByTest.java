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
        String inputQuery = "SELECT * FROM table_name ORDER BY column_name LIMIT 10;";
        //si no tiene limit hay que agregarlo. llama refactor
        String result = refactoring.refactor(inputQuery);
        assertEquals("SELECT * FROM table_name LIMIT 10;", result);
    }
}

//verificar que hacemos si hay una subquery? 