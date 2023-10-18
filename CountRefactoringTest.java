import static org.junit.Assert.assertFalse;
import org.junit.Test;

class CountRefactoringTest{
    
    @Test 
    public void changingStarRefactorTest() throws RefactoringException {
        String result;
        Refactoring refactoring = new CountRefactoring();
        result = refactoring.refactor("SELECT * FROM table_name;");  
        assertFalse(result.contains("*"));
    }

}