import static org.junit.Assert.assertTrue;
import java.beans.Transient;
import org.junit.Test;

public class CountRefactoringTest {
    
    @Test 
    public void changingStarRefactorTest() throws RefactoringException {
        String result;
        Refactoring refactoring = new CountRefactoring();
        result = refactoring.refactor("SELECT COUNT(*) FROM alumnos WHERE dni>25000000;");
        System.out.println(result);
        assertTrue(result.contains("COUNT"));
    }

}