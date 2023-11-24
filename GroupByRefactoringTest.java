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
    public void checkQueryAfterTransformWithoutGroupBY() throws RefactoringException{
        String result; 
        Refactoring refactoring = new GroupByRefactoring();        
        ((GroupByRefactoring) refactoring).setStmtParameter("P.name,P.edad");
        result = refactoring.refactor("SeleCT P.name,P.edad FROM PERSONA P;");//Caso exitoso
        assertEquals("SeleCT P.name,P.edad FROM PERSONA P GROUP BY P.name,P.edad;",result);
        
    }

    @Test
    public void checkQueryAfterTransformWithGroupBY() throws RefactoringException{
        String result; 
        Refactoring refactoring = new GroupByRefactoring();        
        ((GroupByRefactoring) refactoring).setStmtParameter("D.direccion,D.numero");
        result = refactoring.refactor("SELECT D.direccion,D.numero FROM DOMICILIO D GROUP BY D.direccion,D.numero;");//Caso con group by
        System.out.println("Resultado");
        System.out.println(result);
        assertEquals("SELECT D.direccion,D.numero FROM DOMICILIO D GROUP BY D.direccion,D.numero;",result);
        
    }

  

 



}