import static org.junit.Assert.assertTrue;

import java.beans.Transient;

import org.junit.Test;

public class LikeRefactoringTest {
    private LikeRefactoring refactoring;
    private String result;

    @Test
    public void LikeRefactoringFirst() throws RefactoringException {
        this.refactoring = new LikeRefactoring();
        this.result = refactoring.refactor("SELECT * FROM table_name WHERE nombre LIKE '%ar';");
        assertTrue(result.equals("SELECT * FROM table_name WHERE nombre LIKE 'ar%';"));
    }
 
    @Test
    public void LikeRefactoringBetween() throws RefactoringException {
        this.refactoring = new LikeRefactoring();
        this.result = refactoring.refactor("SELECT * FROM table_name WHERE nombre like '%ar%';");
        assertTrue(result.equals("SELECT * FROM table_name WHERE nombre like 'ar%';"));
    }

    @Test
    public void LikeRefactoringInMiddle() throws RefactoringException {
        this.refactoring = new LikeRefactoring();
        this.result = refactoring.refactor("SELECT * FROM table_name WHERE nombre Like 'a%r';");
        assertTrue(result.equals("SELECT * FROM table_name WHERE nombre Like 'ar%';"));
    }

    @Test
    public void LikeRefactoringInMiddleAndFirst() throws RefactoringException {
        this.refactoring = new LikeRefactoring();
        this.result = refactoring.refactor("SELECT * FROM table_name WHERE nombre liKE '%a%r';");
        assertTrue(result.equals("SELECT * FROM table_name WHERE nombre liKE 'ar%';"));
    }

    @Test //no cumple precondicion, ya es valido
    public void LikeRefactoringEnd() throws RefactoringException { 
        this.refactoring = new LikeRefactoring();
        try{
            this.refactoring.refactor("SELECT * FROM table_name WHERE nombre LIKE 'ar%';");
        }
        catch (Exception e){
            assertTrue(e.getMessage().toString().equals("Preconditions not met."));
        }
    }

    @Test //no cumple con las precondiciones (no tiene %)
    public void NoneLikeRefactoring() throws RefactoringException {
        this.refactoring = new LikeRefactoring();
        try{
            this.refactoring.refactor("SELECT * FROM table_name WHERE nombre LIKE 'ar';");
        }
        catch (Exception e){
            assertTrue(e.getMessage().toString().equals("Preconditions not met."));
        }
    }

    @Test //no cumple con las precondiciones (consulta no valida)
    public void NoneRefactoringRandomString() throws RefactoringException {
        this.refactoring = new LikeRefactoring();
        try{
            this.refactoring.refactor("CREATE TABLE consu%ltas;");
        }
        catch (Exception e){
            assertTrue(e.getMessage().toString().equals("Preconditions not met."));
        }
    }

    @Test //no cumple con las precondiciones (no tiene LIKE)
    public void NoneRefactoringNonExistingLike() throws RefactoringException {
        this.refactoring = new LikeRefactoring();
        try{
            this.refactoring.refactor("SELECT * FROM table_name;");
        }
        catch (Exception e){
            assertTrue(e.getMessage().toString().equals("Preconditions not met."));
        }
    }

    @Test //no cumple con las precondiciones (no es consulta valida)
    public void NoneRefactoringWildcardNotInLike() throws RefactoringException {
        this.refactoring = new LikeRefactoring();
        try{
            this.refactoring.refactor("SELECT * FROM table_na%me WHERE nombre LIKE 'ar';");
        }
        catch (Exception e){
            assertTrue(e.getMessage().toString().equals("Preconditions not met."));
        }
    }

}
