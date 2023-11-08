import static org.junit.Assert.assertTrue;

import java.beans.Transient;

import org.junit.Test;


public class SelectingSpecificColumnsRefactoringTest {

    @Test 
    public void SelectingSpecificColumnsRefactorAQueryAsterisco() {
        String result;
        Refactoring refactoring = new SelectingSpecificColumnsRefactoring();
        result = refactoring.refactor("SELECT * FROM table_name;");
        result = result.substring(0,result.indexOf("FROM"));
        assertFalse(result.contains("*"));
    }

    @Test 
    public void SelectingSpecificColumnsRefactorAQuerySubquery() {
        String result;
        Refactoring refactoring = new SelectingSpecificColumnsRefactoring();
        result = refactoring.refactor("SELECT * FROM table_name;");
        result = result.substring(0,result.indexOf("FROM"));
        assertFalse(result.contains("("));
    }

    @Test 
    public void SelectingSpecificColumnsRefactorAQueryColumnaInexistente() throws RefactoringException {
        String result;
        Refactoring refactoring = new SelectingSpecificColumnsRefactoring();
        result = refactoring.refactor("SELECT * FROM table_name;");
        result = result.substring(0,result.indexOf("FROM"));
        assertFalse(result.contains("*"));
    }



}
