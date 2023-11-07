import static org.junit.Assert.assertTrue;

import java.beans.Transient;

import org.junit.Test;


public class SelectingSpecificColumnsRefactoringTest {

    @Test 
    public void SelectingSpecificColumnsRefactorAQuery() throws RefactoringException {
        String result;
        Refactoring refactoring = new SelectingSpecificColumnsRefactoring();
        result = refactoring.refactor("SELECT * FROM table_name;");  
        assertTrue(result.length() == 27);
 
    }

    @Test 
    public void SelectingSpecificColumnsRefactorABadQuery()  {
        
        boolean failure = false;
        Refactoring refactoring = new SelectingSpecificColumnsRefactoring();
        try{
            refactoring.refactor("FOOBAR * WHERE 1=1;");  
        }
        catch(Exception e) { failure = true; }

        assertTrue(failure);
            
    }

     @Test 
    public void SelectingSpecificColumnsRefactorAQueryAsterisco() throws RefactoringException {
        String result, textoNoEsperado ="SELECT *";
        Refactoring refactoring = new SelectingSpecificColumnsRefactoring();
        result = refactoring.refactor("SELECT * FROM table_name;");
        result = result.substring(0,8);
        assertTrue(textoNoEsperado.equals(result));
    }



}
