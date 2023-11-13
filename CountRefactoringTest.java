import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.Test;
import java.util.List;

public class CountRefactoringTest {
    
    @Test 
    public void changingStarRefactorTest() throws RefactoringException {
        String result;
        Refactoring refactoring = new CountRefactoring();
        //result = refactoring.refactor("SELECT COUNT( * ) FROM alumnos WHERE (dni>25000000 AND dni<30000000);");
        //assertEquals("SELECT COUNT( alumnos ) FROM alumnos WHERE (dni>25000000 AND dni<30000000);", result); Need func
    }

    @Test
    public void preConditionsCheck()  {
        boolean withoutStar = false;
        Refactoring refactoring = new CountRefactoring();
        try{
            refactoring.refactor("SELECT COUNT (column)  FROM A WHERE ID > 1 GROUP BY HAVING (age > 17);"); //Need STAR (*)
        }
        catch(Exception e) {
            if (e.getMessage().equals("Preconditions not met."))
                withoutStar = true;
        }
        assertTrue(withoutStar);
    }
    @Test
    public void validatedVariantsofCount()  {
        boolean correctQuery = true;
        Refactoring refactoring = new CountRefactoring();
        List<String> variants = getDifferentCountLayout();
        try{
            for (String query: variants) {
                refactoring.refactor(query);
            }
        }
        catch(Exception e) {
            System.out.println(e.getMessage()   );
            correctQuery = false;
        }
        assertTrue(correctQuery);
    }
    
    private List<String> getDifferentCountLayout(){
        return List.of("SELECT CoUnT (*) FROM Liebres;",
                "SELECT CoUNt(*) FROM Condores;",
                "SELECT COUNt(  *  ) from Ratas;",
                "SELECT COUNt (  *) from Leones;");
    }

}