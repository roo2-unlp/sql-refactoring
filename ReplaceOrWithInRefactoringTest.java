import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.beans.Transient;

import org.junit.Test;
public class ReplaceOrWithInRefactoringTest{

	@Test 
    public void checkPreconditionsTrue() throws RefactoringException {
        Refactoring refactoring = new ReplaceOrWithInRefactoring();
        //assertTrue(refactoring.checkPreconditions("SELECT * FROM empleados WHERE estado_civil = 'Soltero' OR estado_civil = 'Casado' OR estado_civil = 'Divorciado';"));
        assertTrue(refactoring.checkPreconditions("SELECT * FROM empleados WHERE estado_civil = 'Soltero' OR estado_civil = 'Casado';"));
    }

    @Test 
    public void checkPreconditionsFalse() throws RefactoringException {
        Refactoring refactoring = new ReplaceOrWithInRefactoring();
        assertFalse(refactoring.checkPreconditions("SELECT * FROM empleados WHERE estado_civil = 'Soltero'"));
    }

    // @Test 
    // public void replaceOrWithInRefactorABadQuery()  {
        
    //     boolean failure = false;
    //     Refactoring refactoring = new NullRefactoring();
    //     try{
    //         refactoring.refactor("SELECT * FROM empleados WHERE estado_civil = 'Soltero'");  
    //     }
    //     catch(Exception e) { failure = true; }

    //     assertTrue(failure);
            
    // }

}
