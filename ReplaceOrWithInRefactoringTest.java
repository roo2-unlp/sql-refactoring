import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import java.beans.Transient;

import org.junit.Test;

public class ReplaceOrWithInRefactoringTest {

    @Test
    public void TestRefactorCorrecto() throws RefactoringException {
        System.out.println("----------------------------");
        System.out.println("Realizando TestRefactorCorrecto");
        Refactoring refactoring = new ReplaceOrWithInRefactoring();
        String consulta = "SELECT nombre,apellido FROM empleados WHERE estado_civil = 'Soltero' OR estado_civil = 'Casado'";
        String consultaFinal = "SELECT nombre,apellido FROM empleados WHERE estado_civil IN('Soltero','Casado')";
        assertTrue(refactoring.refactor(consulta).equals(consultaFinal));
        
    }

    @Test
    public void TestRefactorFallidoDistintosCampos() {        
        
        System.out.println("----------------------------");
        System.out.println("Realizando TestRefactorFallidoDistintosCampos");
        Refactoring refactoring = new ReplaceOrWithInRefactoring();
        String consulta = "SELECT * FROM empleados WHERE estado_civil = 'Soltero' OR estadocivil = 'Casado'";
        boolean error = false;
        try {
            refactoring.refactor(consulta);
        } catch (RefactoringException e) {
            //Al tener dos campos distintos dentro del WHERE lanza la excepcion Preconditions not met
            error = true;
        }
        assertTrue(error);
    }

    @Test
    public void TestRefactorFallidoFaltaOr() {        
        
        System.out.println("----------------------------");
        System.out.println("Realizando TestRefactorFallidoFaltaOr");
        Refactoring refactoring = new ReplaceOrWithInRefactoring();
        String consulta = "SELECT * FROM empleados WHERE estado_civil = 'Soltero'";
        boolean error = false;
        try {
            refactoring.refactor(consulta);
        } catch (RefactoringException e) {
            //Al faltar la palabra clave OR dentro del WHERE lanza la excepcion Preconditions not met
            error = true;
        }
        assertTrue(error);
    }

    @Test
    public void TestRefactorFallidoSintacticamente() {        
        
        System.out.println("----------------------------");
        System.out.println("Realizando TestRefactorFallidoSintacticamente");
        Refactoring refactoring = new ReplaceOrWithInRefactoring();
        String consulta = "SELEC * FROM empleados WHERE estado_civil = 'Soltero' OR estado_civil = 'Casado'";
        boolean error = false;
        try {
            refactoring.refactor(consulta);
        } catch (RefactoringException e) {
            //Al fallar sintacticamente da error por no encontrar la palabra clave SELECT
            // y a su vez lanza la excepcion Preconditions not met
            error = true;
        }
        assertTrue(error);
    }

    @Test
    public void TestRefactorFallidoComparacionIncorrectaWhere() {      
        System.out.println("----------------------------");  
        System.out.println("Realizando TestRefactorFallidoComparacionIncorrectaWhere");
        Refactoring refactoring = new ReplaceOrWithInRefactoring();
        String consulta = "SELECT * FROM empleados WHERE edad > 18 or edad = 65";
        boolean error = false;
        try {
            refactoring.refactor(consulta);
        } catch (RefactoringException e) {
            //Al tener operandos distintos de igual dentro del WHERE lanza la excepcion Preconditions not met por no cumplir con la precondicion.
            error = true;
        }
        assertTrue(error);
    }

     @Test
    public void TestRefactorFallidoTieneAnd() {        
        
        System.out.println("----------------------------");
        System.out.println("Realizando TestRefactorFallidoTieneAnd");
        Refactoring refactoring = new ReplaceOrWithInRefactoring();
        String consulta = "SELECT * FROM empleados WHERE estado_civil = 'Soltero' AND edad = 20";
        boolean error = false;
        try {
            refactoring.refactor(consulta);
        } catch (RefactoringException e) {
            //Al tener un AND dentro del WHERE lanza la excepcion Preconditions not met ya que no puede realizar la transformacion.
            error = true;
        }
        assertTrue(error);
    }
    
    @Test
    public void TestRefactorFallidoConsultaInsert() {        
        
        System.out.println("----------------------------");
        System.out.println("Realizando TestRefactorFallidoConsultaInsert");
        Refactoring refactoring = new ReplaceOrWithInRefactoring();
        String consulta = "INSERT INTO empleados (estado_civil) VALUES ('Divorciado');";
        boolean error = false;
        try {
            refactoring.refactor(consulta);
        } catch (RefactoringException e) {
            //Al ser una consulta de agregacion y no cumplir con ninguna precondicion lanza la excepcion Preconditions not met.
            error = true;
        }
        assertTrue(error);
    }

    @Test
    public void TestRefactorFallidoConsultaCreate() {        
        
        System.out.println("----------------------------");
        System.out.println("Realizando TestRefactorFallidoConsultaCreate");
        Refactoring refactoring = new ReplaceOrWithInRefactoring();
        String consulta = "CREATE TABLE empleados (id INTEGER PRIMARY KEY, nombre TEXT, apellido TEXT, edad INTEGER, estado_civil TEXT);";
        boolean error = false;
        try {
            refactoring.refactor(consulta);
        } catch (RefactoringException e) {
            //Al ser una consulta de creacion de una tabla no cumple con las precondiciones por lo tanto arrojara la excepcion Preconditions not met
            error = true;
        }
        assertTrue(error);
    }


}
