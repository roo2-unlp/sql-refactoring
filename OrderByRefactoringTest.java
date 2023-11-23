import static org.junit.Assert.*;
import java.beans.Transient;
import org.junit.Test;

public class OrderByRefactoringTest {

    @Test
    public void testTransformAddOrderBy() throws RefactoringException{
        Refactoring refactoring = new RefactoringOrderBy();
        String query = "SELECT P.NOMBRE FROM PERSONA P";
        String result = refactoring.refactor(query);
        assertEquals(result,"SELECT P.NOMBRE FROM PERSONA P ORDER BY P.NOMBRE");
    }

    //Test para verificar que tipo de consulta es, si la consulta es distinta a SELECT ... -> no hace nada
    @Test
    public void testOtroTipoConsulta() {
  		 String sentenciaSQL = "Insert into Persona(nombre,edad,dni);";
  		 Refactoring refactoring = new RefactoringOrderBy();
   		try {
      			String sentenciaTransformada = refactoring.refactor(sentenciaSQL);
      			assertTrue(sentenciaTransformada.contains("Order By"));
  		 } catch (RefactoringException e) {
    		  	System.out.println(e.getMessage() + "No se puedo transformar sentencia. Sentencia Final: " + sentenciaSQL);
      		    
   		}
	}
    
    
    
    
    
    
    
    
    
    
    //Estos test nada mas sirve para ver si las precondiciones y postcondiones funcionan.
    
    // Test para verificar que la sentencia ya tiene el order by.
    @Test
    public void testPrecondicionConOrderBy() {
   		String sentenciaSQL = "Select nombre,dni from persona ORDER BY nombre";
   		RefactoringOrderBy refactoring = new RefactoringOrderBy();

   		assertTrue(refactoring.checkPreconditions(sentenciaSQL));
   	}

   	// Test para verificar que la sentencia tiene error de sintaxis
    @Test
    public void testPrecondicionErrorSintaxis(){
   		String sentenciaSQL = "Sel nombre,dni from persona";
   		RefactoringOrderBy refactoring = new RefactoringOrderBy();

   		assertFalse(refactoring.checkPreconditions(sentenciaSQL));
   	}
  
 
    // Test para verificar que la sentencia no tiene el order by.
    @Test
    public void testPrecondicionSinOrderBy(){
 		String sentenciaSQL = "Select nombre,dni from persona";
 		RefactoringOrderBy refactoring = new RefactoringOrderBy();

 		assertFalse(refactoring.checkPreconditions(sentenciaSQL));
 	}

 	
 	// Test para verificar que la sentencia ya tiene el Order by luego de la transformacion
    @Test
    public void testPostcondicionTransformacion(){
 		String sentenciaSQL = "Select nombre,dni from persona";
 		RefactoringOrderBy refactoring = new RefactoringOrderBy();

 		assertTrue(refactoring.checkPostconditions(sentenciaSQL));
 	}

}