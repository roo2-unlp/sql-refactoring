import static org.junit.Assert.*;
import java.beans.Transient;
import org.junit.Test;

public class LimitWithOrderByTest {

    // Caso de prueba: Consulta inválida
    // deberia retornar "Preconditions not met."
    @Test
    public void testInvalidQuery() throws RefactoringException{
        LimitWithOrderBy refactoring = new LimitWithOrderBy();    
        String inputQuery = "Redictado objetos 2";
        try{
            refactoring.refactor(inputQuery);
        }
        catch (RefactoringException e){
            assertEquals("Preconditions not met.", e.getMessage());
            System.out.println("Consulta SQL no válida.");
        }
    }
    
    // Caso de prueba: Consulta inválida sin ORDER BY
    // deberia retornar "Preconditions not met."
    @Test
    public void testLimitWithOrderByInvalidQuery() throws RefactoringException{
        LimitWithOrderBy refactoring = new LimitWithOrderBy();    
        String inputQuery = "SELECT * FROM partidos;";
        try{
            refactoring.refactor(inputQuery);
        }
        catch (RefactoringException e){
            assertEquals("Preconditions not met.", e.getMessage());
        }
    }
    
    // Caso de prueba: Consulta válida con ORDER BY pero sin LIMIT
    @Test
    public void testLimitWithOrderByRefactor() throws RefactoringException {
        LimitWithOrderBy refactoring = new LimitWithOrderBy();
        String inputQuery = "SELECT nombre, apellido FROM partidos ORDER BY nombre;";        
        String result = refactoring.refactor(inputQuery);
        assertEquals("SELECT nombre, apellido FROM partidos ORDER BY nombre LIMIT 10;", result);
    }
    
    // Caso de prueba: Consulta válida con ORDER BY pero con LIMIT
    @Test
    public void testLimitWithOrderByRefactorLimit() throws RefactoringException {
        LimitWithOrderBy refactoring = new LimitWithOrderBy();
        String inputQuery = "SELECT nombre, apellido FROM personas ORDER BY nombre, apellido LIMIT 10;";        
        String result = refactoring.refactor(inputQuery);
        assertEquals("SELECT nombre, apellido FROM personas ORDER BY nombre, apellido LIMIT 10;", result);
    }
    
    // setea el valor del limit
    @Test
    public void testTransformAddLimitWithOrderByAndCustomLimit() throws RefactoringException {
        LimitWithOrderBy refactoring = new LimitWithOrderBy();
        String inputQuery = "SELECT P.NOMBRE FROM PERSONA P ORDER BY P.NOMBRE";
        refactoring.setLimit(5);
        String result = refactoring.refactor(inputQuery); 
        assertEquals(result, "SELECT P.NOMBRE FROM PERSONA P ORDER BY P.NOMBRE LIMIT 5;");
    }

    // QUE PASA SI LA CONSULTA YA TIENE LIMIT Y APARTE LE SETEO EL LIMIT ?
    @Test
    public void testTransformAddLimitWithOrderByAndCustomLimitExist() throws RefactoringException {
        LimitWithOrderBy refactoring = new LimitWithOrderBy();
        String inputQuery = "SELECT P.NOMBRE FROM PERSONA P ORDER BY P.NOMBRE LIMIT 10;";
        refactoring.setLimit(5);
        String result = refactoring.refactor(inputQuery); 
        assertEquals(result, "SELECT P.NOMBRE FROM PERSONA P ORDER BY P.NOMBRE LIMIT 10;");
    }

    // PROBAR SI LAS SUBCONSULTAS PASAN IGUAL
    @Test
    public void testTransformAddLimitWithOrderBySub() throws RefactoringException {
        LimitWithOrderBy refactoring = new LimitWithOrderBy();
        String inputQuery = "SELECT a.nombre FROM alumnos UNION SELECT p.nombre FROM profesores ORDER BY nombre";
        String result = refactoring.refactor(inputQuery); 
        assertEquals(result, "SELECT a.nombre FROM alumnos UNION SELECT p.nombre FROM profesores ORDER BY nombre LIMIT 10;");
    }
    
} 
