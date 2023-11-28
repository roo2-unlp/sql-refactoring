import static org.junit.Assert.assertTrue;

import java.beans.Transient;

import org.junit.Test;

public class GroupByRefactoringTest {
    
    @Test 
    public void preconditionsTest() throws RefactoringException {
        String result;
        Refactoring refactoring = new GroupByRefactoring();
        //result = refactoring.refactor("SELECT products FROM table_name;");  
        //assertTrue(refactoring.checkPreconditions("SELECT * FROM clientes WHERE cliente_id IN (SELECT distinct cliente_id FROM pedidos);;"));
        assertTrue(refactoring.checkPreconditions("SELECT DISTINCT producto, SUM(cantidad) as total_ventas FROM ventas"));
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
