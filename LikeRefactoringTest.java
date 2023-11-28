import static org.junit.Assert.assertTrue;

import java.beans.Transient;

import org.junit.Test;

public class LikeRefactoringTest {

    @Test
    public void LikeRefactoringFirst() throws RefactoringException {
        LikeRefactoring refactoring = new LikeRefactoring();
        String result = refactoring.refactor("SELECT * FROM table_name WHERE nombre LIKE '%ar';");
        System.out.println("el resultado de todo el refactor es: "+result);
        assertTrue(result.equals("SELECT * FROM table_name WHERE nombre LIKE 'ar%';"));
    }
 /* 
    @Test
    public void LikeRefactoringBetween() throws RefactoringException {
        LikeRefactoring refactoring = new LikeRefactoring();
        String result = refactoring.refactor("SELECT * FROM table_name WHERE nombre LIKE '%ar%';");
        assertTrue(result.equals("SELECT * FROM table_name WHERE nombre LIKE 'ar%';"));
    }

    @Test
    public void LikeRefactoringInMiddle() throws RefactoringException {
        LikeRefactoring refactoring = new LikeRefactoring();
        String result = refactoring.refactor("SELECT * FROM table_name WHERE nombre LIKE 'a%r';");
        assertTrue(result.equals("SELECT * FROM table_name WHERE nombre like 'ar%';"));
    }

    @Test
    public void LikeRefactoringEnd() throws RefactoringException {
        LikeRefactoring refactoring = new LikeRefactoring();
        String result = refactoring.refactor("SELECT * FROM table_name WHERE nombre LIKE 'ar%';");
        assertTrue(result.equals("SELECT * FROM table_name WHERE nombre LIKE 'ar%';"));
    }

    @Test
    public void LikeRefactoringInMiddleAndFirst() throws RefactoringException {
        LikeRefactoring refactoring = new LikeRefactoring();
        String result = refactoring.refactor("SELECT * FROM table_name WHERE nombre LIKE '%a%r';");
        assertTrue(result.equals("SELECT * FROM table_name WHERE nombre LIKE 'ar%';"));
    }

    @Test
    public void NoneLikeRefactoring() throws RefactoringException {
        LikeRefactoring refactoring = new LikeRefactoring();
        String result = refactoring.refactor("SELECT * FROM table_name WHERE nombre LIKE 'ar';");
        assertTrue(result.equals("SELECT * FROM table_name WHERE nombre LIKE 'ar';"));
    }

    @Test
    public void NoneRefactoringRandomString() throws RefactoringException {
        LikeRefactoring refactoring = new LikeRefactoring();
        String result = refactoring.refactor("CREATE TABLE consu%ltas;");
        assertTrue(result.equals("CREATE TABLE consultas;"));
        result = refactoring.refactor("Hola");
        assertTrue(result.equals("Hola"));
    }

    @Test
    public void NoneRefactoringNonExistingLike() throws RefactoringException {
        LikeRefactoring refactoring = new LikeRefactoring();
        String result = refactoring.refactor("SELECT * FROM table_name;");
        assertTrue(result.equals("SELECT * FROM table_name;"));
    }

    @Test
    public void NoneRefactoringWildcardNotInLike() throws RefactoringException {
        LikeRefactoring refactoring = new LikeRefactoring();
        String result = refactoring.refactor("SELECT * FROM table_na%me WHERE nombre LIKE 'ar';");
        assertTrue(result.equals("SELECT * FROM table_na%me WHERE nombre LIKE 'ar';"));
    }
*/
}
