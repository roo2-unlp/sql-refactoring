import static org.junit.Assert.*;


import java.beans.Transient;

import org.junit.Test;

public class OrderByRefactoringTest {

    @Test
    public void testTransformAddOrderBy() throws RefactoringException{
        Refactoring refactoring = new OrderByRefactoring();
        String query = "SELECT P.NOMBRE FROM PERSONA P";
        String result = refactoring.refactor(query);
        assertEquals(result,"SELECT P.NOMBRE FROM PERSONA P ORDER BY P.NOMBRE");
    }

    // Test para verificar que la sentencia ya tiene el order by
    @Test
    public void testPrecondicionConOrderBy() {
   		String sentenciaSQL = "Select nombre,dni from persona ORDER BY nombre";
   		RefactoringOrderBy refactoring = new RefactoringOrderBy();

   		assertTrue(refactoring.checkPrenconditions(sentenciaSQL));
   	}

   	// Test para verificar que la sentencia tiene error de sintaxis
    @Test
    public void testPrecondicionErrorSintaxis(){
   		String sentenciaSQL = "Select nombre,dni from persona Where";
   		RefactoringOrderBy refactoring = new RefactoringOrderBy();

   		assertFalse(refactoring.checkPrenconditions(sentenciaSQL));
   	}
  
 
    // Test para verificar que la sentencia no tiene el order by
    @Test
    public void testPrecondicionSinOrderBy(){
 		String sentenciaSQL = "Select nombre,dni from persona";
 		RefactoringOrderBy refactoring = new RefactoringOrderBy();

 		assertFalse(refactoring.checkPrenconditions(sentenciaSQL));
 	}

 	
 	// Test para verificar que la sentencia ya tiene el Order by luego de la transformacion
    @Test
    public void testPostcondicionTransformacion(){
 		String sentenciaSQL = "Select nombre,dni from persona";
 		RefactoringOrderBy refactoring = new RefactoringOrderBy();

 		assertTrue(refactoring.checkPostconditions(sentenciaSQL));
 	}

}