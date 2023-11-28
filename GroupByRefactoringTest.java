import static org.junit.Assert.*;

import java.beans.Transient;

import org.junit.Test;

public class GroupByRefactoringTest {

    @Test
    public void checkQueryAfterTransformWithoutGroupBY() throws RefactoringException {
        String result;
        Refactoring refactoring = new GroupByRefactoring();
        String queryToTransform = "SeleCT P.name,P.edad FROM PERSONA P;";
        ((GroupByRefactoring) refactoring).setStmtParameter("P.name,P.edad");
        result = refactoring.refactor(queryToTransform);
        assertEquals("SeleCT P.name,P.edad FROM PERSONA P GROUP BY P.name,P.edad;", result);
    }

    @Test
    public void checkQueryWithWhereWithoutGroupBy() throws RefactoringException {
        String result;
        String queryToTransform = "SELECT R.direccion,R.numero FROM REVOR R WHERE R.numero=22;";
        Refactoring refactoring = new GroupByRefactoring();
        ((GroupByRefactoring) refactoring).setStmtParameter("R.direccion,R.numero");
        result = refactoring.refactor(queryToTransform);              
        assertEquals("SELECT R.direccion,R.numero FROM REVOR R WHERE R.numero=22 GROUP BY R.direccion,R.numero;", result);
    }
    @Test
    public void checkQueryWithGroupBy() throws RefactoringException {
        String result;
        String queryToTransform = "SELECT S.NAME FROM SUPRA S WHERE S.EDAD > 18 GROUP BY S.NAME";
        Refactoring refactoring = new GroupByRefactoring();
        ((GroupByRefactoring) refactoring).setStmtParameter("S.NAME");
        boolean failure = false;
        result = refactoring.refactor(queryToTransform);
        assertEquals(queryToTransform, result);
    }

     @Test
    public void checkQueryWithBadSizeParameterWithoutGroupBy() throws RefactoringException {
        String result;
        String queryToTransform = "SELECT R.direccion,R.numero FROM REVOR R WHERE R.numero=20";
        Refactoring refactoring = new GroupByRefactoring();
        ((GroupByRefactoring) refactoring).setStmtParameter("R.direccion");
        result = refactoring.refactor(queryToTransform);        
        assertNotEquals("SELECT R.direccion,R.numero FROM REVOR R WHERE R.numero=20 GROUP BY R.direccion,R.numero;", result);
    }

     @Test
    public void checkQueryWithoutGroupBy() throws RefactoringException {
        String result;
        String queryToTransform = "SELECT R.direccion,R.numero FROM REVOR R WHERE R.direccion='calle 54'";
        Refactoring refactoring = new GroupByRefactoring();
        ((GroupByRefactoring) refactoring).setStmtParameter("R.direccion,R.numero");
        result = refactoring.refactor(queryToTransform);        
        assertEquals("SELECT R.direccion,R.numero FROM REVOR R WHERE R.direccion='calle 54' GROUP BY R.direccion,R.numero;", result);
    }


    //Seria redundante dejar tb este TEST? 
    // @Test
    // public void checkValidationsOfBadSizeOfParameterAssignToGroupBy() throws RefactoringException {
    //     String result;
    //     Refactoring refactoring = new GroupByRefactoring();
    //     String queryToTransform = "SeleCT P.name,P.edad FROM PERSONA P;";
    //     ((GroupByRefactoring) refactoring).setStmtParameter("P.name");
    //     Boolean failure = false;
    //     try{
    //         result = refactoring.refactor(queryToTransform);
    //     }catch(Exception e){
    //         System.out.println("La excepcion fue: " + e.getMessage());
    //         failure=true;
    //     }        
    //     assertTrue(failure);
    // }


    

}