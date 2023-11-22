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
        ((GroupByRefactoring) refactoring).setStmtParameter("P.name,p.edad");
        result = refactoring.refactor("SeleCT P.name,p.edad FROM PERSONA P;");//Caso exitoso
        //result = refactoring.refactor("SELECT * FROM PERSONA P GROUP BY p.Name"); //Caso que tiene un problema en el select
        
        assertEquals("Select P.name FROM PERSONA P GROUP BY P.name;",result);
        //TODO chequear que el formato del group by sea el correcto
    }

    @Test
    public void testPreConditions() throws RefactoringException{
        String result;
        Refactoring refactoring = new GroupByRefactoring();
        result = refactoring.refactor("SELECT * FROM table_name GROUP BY *;");
        GroupByRefactoring res = res.checkPreconditions(result);
        assertTrue(res); 
    }
    
    @Test
    public void testPostConditions() throws RefactoringException{
        String result;
        Refactoring refactoring = new GroupByRefactoring();
        result = refactoring.refactor("SELECT * FROM table_name GROUP BY *;");
        GroupByRefactoring res = res.checkPostconditions(result);
        assertTrue(res);
    }

    @Test
    public void testTransformAddGroupBy() throws RefactoringException{
        Refactoring refactoring = new GroupByRefactoring();
        ((GroupByRefactoring) refactoring).setStmtParameter("P.DNI,P.NOMBRE,P.APELLIDO");
        String result = refactoring.transform("SELECT P.DNI,P.NOMBRE,P.APELLIDO FROM PERSONA P");
        assertEquals(result,"SELECT P.DNI,P.NOMBRE,P.APELLIDO FROM PERSONA P GROUP BY P.DNI,P.NOMBRE,P.APELLIDO;");
    }

    //@Test
    //public void testTransformDistinct() throws RefactoringException{
    //    Refactoring refactoring = new GroupByRefactoring();
    //    String result = refactoring.transform("SELECT DISTINCT(P.NOMBRE) FROM PERSONA P;");
    //    assertEquals(result,"SELECT DISTINCT(P.NOMBRE) FROM PERSONA P;");
    //}

    @Test
    public void testTransforAgregationFunction() throws RefactoringException{
        Refactoring refactoring = new GroupByRefactoring();
        ((GroupByRefactoring) refactoring).setStmtParameter("V.PATENTE");
        String result = refactoring.transform("SELECT V.PATENTE,MAX(V.PESO) FROM VEHICULO V");
        assertEquals(result,"SELECT V.PATENTE,MAX(V.PESO) FROM VEHICULO V GROUP BY V.PATENTE;");
    }



}