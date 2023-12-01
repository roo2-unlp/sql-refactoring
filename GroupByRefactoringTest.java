import static org.junit.Assert.*;

import java.beans.Transient;

import org.junit.Test;

public class GroupByRefactoringTest {
    
    @Test
    public void checkQueryAfterTransformWithoutGroupBy_ThenGroupByAdded() throws RefactoringException {
        String result;
        Refactoring refactoring = new GroupByRefactoring();
        String queryToTransform = "SeleCT P.name,P.edad FROM PERSONA P;";
        ((GroupByRefactoring) refactoring).setStmtParameter("P.name,P.edad");
        result = refactoring.refactor(queryToTransform);
        assertEquals("SeleCT P.name,P.edad FROM PERSONA P GROUP BY P.name,P.edad;", result);
    }

    @Test
    public void checkQueryWithNotValidParameterWithoutGroupBy_ThenGroupByWillNotBeAdded() throws RefactoringException {
        String result;
        Refactoring refactoring = new GroupByRefactoring();
        String queryToTransform = "SeleCT P.name,P.edad FROM PERSONA P;";
        ((GroupByRefactoring) refactoring).setStmtParameter("P.names");
        boolean failure = false;
        try {
            result = refactoring.refactor(queryToTransform);
        } catch (Exception e) {
            failure = true;
        }
        assertTrue(failure);
    }

    @Test
    public void checkQueryWithOrderByWithoutGroupBy_ThenGroupByAdded() throws RefactoringException {
        String result;
        Refactoring refactoring = new GroupByRefactoring();
        String queryToTransform = "SeleCT P.name,P.edad FROM PERSONA P ORDER BY P.edad,P.name;";
        ((GroupByRefactoring) refactoring).setStmtParameter("P.name");

        result = refactoring.refactor(queryToTransform);        
        assertEquals("SeleCT P.name,P.edad FROM PERSONA P GROUP BY P.name ORDER BY P.edad,P.name;", result);
    }

    @Test
    public void checkQueryWithAtLeastOneParameterValidWithoutGroupBy_ThenGroupByAdded() throws RefactoringException {
        String result;
        Refactoring refactoring = new GroupByRefactoring();
        String queryToTransform = "SeleCT P.name,P.edad FROM PERSONA P;";
        ((GroupByRefactoring) refactoring).setStmtParameter("P.names,P.edad");
        result = refactoring.refactor(queryToTransform);
        assertEquals("SeleCT P.name,P.edad FROM PERSONA P GROUP BY P.names,P.edad;", result);
    }

    @Test
    public void checkQueryWithWhereWithoutGroupBy_ThenGroupByAdded() throws RefactoringException {
        String result;
        String queryToTransform = "SELECT R.direccion,R.numero FROM REVOR R WHERE R.numero=22;";
        Refactoring refactoring = new GroupByRefactoring();
        ((GroupByRefactoring) refactoring).setStmtParameter("R.direccion,R.numero");
        result = refactoring.refactor(queryToTransform);
        assertEquals("SELECT R.direccion,R.numero FROM REVOR R WHERE R.numero=22 GROUP BY R.direccion,R.numero;",
                result);
    }

    @Test
    public void checkQueryWithGroupBy_ThenTransformNotBeNecessary() throws RefactoringException {
        String result;
        String queryToTransform = "SELECT S.NAME FROM SUPRA S WHERE S.EDAD>18 GROUP BY S.NAME;";
        Refactoring refactoring = new GroupByRefactoring();
        ((GroupByRefactoring) refactoring).setStmtParameter("S.NAME");
        boolean failure = false;
        try {
            result = refactoring.refactor(queryToTransform);
        } catch (Exception e) {
            failure = true;
        }
        assertTrue(failure);
    }

    @Test
    public void checkQueryWithBadParameterWithoutGroupBy_ThenTransformNotBeNecessary() throws RefactoringException {
        String result;
        String queryToTransform = "SELECT R.direccion,R.numero FROM REVOR R WHERE R.numero=20;";
        Refactoring refactoring = new GroupByRefactoring();
        ((GroupByRefactoring) refactoring).setStmtParameter("R.dire");
        boolean failure = false;
        try {
            result = refactoring.refactor(queryToTransform);
        } catch (Exception e) {
            failure = true;
        }
        assertTrue(failure);

    }

    @Test
    public void checkQueryWithHavingAndGroupBy_ThenTransformNotBeNecessary() throws RefactoringException {
        String result;
        String queryToTransform = "SELECT S.NAME FROM SUPRA S WHERE S.EDAD > 18 GROUP BY S.NAME HAVING S.NAME;";
        Refactoring refactoring = new GroupByRefactoring();
        ((GroupByRefactoring) refactoring).setStmtParameter("S.NAME");
        boolean failure = false;
        try {
            result = refactoring.refactor(queryToTransform);
        } catch (Exception e) {
            failure = true;
        }
        assertTrue(failure);
    }

    @Test
    public void checkBadQueryWithHavingAndWithoutGroupBy_ThenTransformNotBeNecessary() throws RefactoringException {
        String result;
        String queryToTransform = "SELECT S.NAME FROM SUPRA S WHERE S.EDAD > 18 HAVING S.NAME;";
        Refactoring refactoring = new GroupByRefactoring();
        ((GroupByRefactoring) refactoring).setStmtParameter("S.NAME");
        boolean failure = false;
        try {
            result = refactoring.refactor(queryToTransform);
        } catch (Exception e) {
            System.out.println("///////////");
            System.out.println("Test Name: checkBadQueryWithHavingAndWithoutGroupBy_ThenTransformNotBeNecessary");
            System.out.print("Error de la exception: " + e.getMessage());            
            failure = true;
        }
        assertTrue(failure);
    }

    @Test
    public void checkQueryWithDistinctAndWithoutGroupBy_ThenGroupByAdded() throws RefactoringException {
        String result;
        String queryToTransform = "SELECT DISTINCT S.NAME FROM SUPRA S WHERE S.EDAD>18;";
        Refactoring refactoring = new GroupByRefactoring();
        ((GroupByRefactoring) refactoring).setStmtParameter("S.NAME");
        result = refactoring.refactor(queryToTransform);
        assertEquals("SELECT DISTINCT S.NAME FROM SUPRA S WHERE S.EDAD>18 GROUP BY S.NAME;", result);
    }

}