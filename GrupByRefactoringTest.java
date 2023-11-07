import static org.junit.Assert.assertTrue;

import java.beans.Transient;

import org.junit.Test;

public class GrupByRefactoringTest {
    
    @Test 
    public void nullrefactorAQuery() throws RefactoringException {
        String result;
        Refactoring refactoring = new NullRefactoring();
        result = refactoring.refactor("SELECT * FROM table_name;");  
        assertTrue(result.length() == 27);
 
    }

    @Test 
    public void nullrefactorABadQuery()  {
        
        boolean failure = false;
        Refactoring refactoring = new NullRefactoring();
        try{
            refactoring.refactor("FOOBAR * WHERE 1=1;");  
        }
        catch(Exception e) { failure = true; }

        assertTrue(failure);
            
    }    
}
