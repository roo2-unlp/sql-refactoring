import static org.junit.Assert.assertTrue;

import java.beans.Transient;

import org.junit.Test;

public class LikeRefactoringTest {

    @Test
    public void LikeRefactoringFirst() throws RefactoringException {
        String result;
        LikeRefactoring refactoring = new LikeRefactoring();
        result = refactoring.refactor("SELECT * FROM table_name WHERE nombre LIKE '%ar';");
        assertTrue(result.equals("SELECT * FROM table_name WHERE nombre LIKE 'ar%';"));

    }

    @Test
    public void LikeRefactoringBetween() throws RefactoringException {
        String result;
        LikeRefactoring refactoring = new LikeRefactoring();
        result = refactoring.refactor("SELECT * FROM table_name WHERE nombre LIKE '%ar%';");
        assertTrue(result.equals("SELECT * FROM table_name WHERE nombre LIKE 'ar%';"));

    }

    @Test
    public void LikeRefactoringInMiddle() throws RefactoringException {
        String result;
        LikeRefactoring refactoring = new LikeRefactoring();
        result = refactoring.refactor("SELECT * FROM table_name WHERE nombre LIKE 'a%r';");
        assertTrue(result.equals("SELECT * FROM table_name WHERE nombre like 'ar%';"));

    }

    @Test
    public void LikeRefactoringEnd() throws RefactoringException {
        String result;
        LikeRefactoring refactoring = new LikeRefactoring();
        result = refactoring.refactor("SELECT * FROM table_name WHERE nombre LIKE 'ar%';");
        assertTrue(result.equals("SELECT * FROM table_name WHERE nombre LIKE 'ar%';"));

    }

    @Test
    public void LikeRefactoringInMiddleAndFirst() throws RefactoringException {
        String result;
        LikeRefactoring refactoring = new LikeRefactoring();
        result = refactoring.refactor("SELECT * FROM table_name WHERE nombre LIKE '%a%r';");
        assertTrue(result.equals("SELECT * FROM table_name WHERE nombre LIKE 'ar%';"));

    }

    @Test
    public void NoneLikeRefactoring() throws RefactoringException {
        String result;
        LikeRefactoring refactoring = new LikeRefactoring();
        result = refactoring.refactor("SELECT * FROM table_name WHERE nombre LIKE 'ar';");
        assertTrue(result.equals("SELECT * FROM table_name WHERE nombre LIKE 'ar';"));

    }

}
