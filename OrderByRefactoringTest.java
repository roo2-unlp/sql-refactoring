import static org.junit.Assert.*;

import java.beans.Transient;

import org.junit.Test;

public class OrderByRefactoringTest {

    @Test
    public void testTransformAddOrderBy() throws RefactoringException{
        Refactoring refactoring = new OrderByRefactoring();
        String query = "SELECT P.NOMBRE FROM PERSONA P";
        String result = refactoring.refactor(query);
        assertEquals(result,"SELECT P.NOMBRE FROM PERSONA P ORDER BY P.NOMBRE");
    }

}