import static org.junit.Assert.*;

import java.beans.Transient;

import org.junit.Test;

public class GroupByRefactoringTest {

    //Example from null refactoring test
    // @Test
    // public void nullrefactorAQuery() throws RefactoringException {
    //     String result;
    //     Refactoring refactoring = new NullRefactoring();
    //     result = refactoring.refactor("SELECT * FROM table_name;");
    //     assertTrue(result.length() == 27);

    // }

    // @Test
    // public void nullrefactorABadQuery() {

    //     boolean failure = false;
    //     Refactoring refactoring = new NullRefactoring();
    //     try {
    //         refactoring.refactor("SELECTED * WHERE 1=1;");
    //     } catch (Exception e) {
    //         failure = true;
    //     }
    //     assertTrue(failure);
    // }

    // ////////

    @Test
    public void checkGroupByFormat() throws RefactoringException{
        String result; 
        Refactoring refactoring = new GroupByRefactoring();        
        ((GroupByRefactoring) refactoring).setStmtParameter("P.name,P.edad");
        result = refactoring.refactor("SeleCT P.name,P.edad FROM PERSONA P;");//Caso exitoso
        //result = refactoring.refactor("SELECT * FROM PERSONA P GROUP BY p.Name"); //Caso que tiene un problema en el select
        
        assertEquals("SeleCT P.name,P.edad FROM PERSONA P GROUP BY P.name;",result);
        //TODO chequear que el formato del group by sea el correcto
    }

  

 



}