import static org.junit.Assert.assertTrue;
import java.beans.Transient;
import org.junit.Test;

public class CountRefactoringTest {
    
    @Test 
    public void changingStarRefactorTest() throws RefactoringException {
        String result;
        Refactoring refactoring = new CountRefactoring();
        result = refactoring.refactor("SELECT COUNT(*) FROM alumnos WHERE dni>25000000 AND dni<30000000;");
        assertTrue( ! result.contains("*"));
    }
    
    @Test 
    public void queryExampleInLowerCaseTest() throws RefactoringException {
        String result;
        Refactoring refactoring = new CountRefactoring();
        result = refactoring.refactor("select count(*) from alumnos where dni>25000000 and dni<30000000;");
        assertTrue( ! result.contains("*"));
    }

}