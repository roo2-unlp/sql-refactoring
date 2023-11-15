import static org.junit.Assert.assertTrue;

import java.beans.Transient;

import org.junit.Test;

public class GroupByRefactoringTest {
    
    @Test 
    public void preconditionsTest() throws RefactoringException {
        String result;
        Refactoring refactoring = new GroupByRefactoring();
        //result = refactoring.refactor("SELECT products FROM table_name;");  
        assertTrue(refactoring.checkPreconditions("SELECT DISTINCT nombre FROM clientes;"));
 
    }

    // @Test 
    // public void nullrefactorABadQuery()  {
        
    //     boolean failure = false;
    //     Refactoring refactoring = new NullRefactoring();
    //     try{
    //         refactoring.refactor("FOOBAR * WHERE 1=1;");  
    //     }
    //     catch(Exception e) { failure = true; }

    //     assertTrue(failure);
            
    // }

}