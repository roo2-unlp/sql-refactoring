import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import java.beans.Transient;

import org.junit.Test;

public class ReplaceOrWithInRefactoringTest {

    @Test
    public void TestRefactorCorrecto() throws RefactoringException {
        Refactoring refactoring = new ReplaceOrWithInRefactoring();
        String consulta = "SELECT * FROM empleados WHERE estado_civil = 'Soltero' OR estado_civil = 'Casado'";
        String consultaFinal = "SELECT * FROM empleados WHERE estado_civil IN ('Soltero', 'Casado')";
        assertTrue(refactoring.refactor(consulta).equals(consultaFinal));
    }

    @Test
    public void TestRefactorFallidoFaltaOr() {
        Refactoring refactoring = new ReplaceOrWithInRefactoring();
        String consulta = "SELECT * FROM empleados WHERE estado_civil = 'Soltero'";
        try {
            refactoring.refactor(consulta);
            // Si no se lanzó una excepción, la prueba falla
            assertFalse("Se esperaba una excepción, pero no se lanzó.", true);
        } catch (RefactoringException e) {
            // Se lanzó una excepción, puedes hacer afirmaciones adicionales aquí
            assertEquals("Preconditions not met.", e.getMessage());
        }
    }

    @Test
    public void TestRefactorFallidoComparacionIncorrectaWhere() {
        Refactoring refactoring = new ReplaceOrWithInRefactoring();
        String consulta = "SELECT * FROM empleados WHERE edad > 18 or edad < 65";
        try {
            refactoring.refactor(consulta);
            // Si no se lanzó una excepción, la prueba falla
            assertFalse("Se esperaba una excepción, pero no se lanzó.", true);
        } catch (RefactoringException e) {
            // Se lanzó una excepción, puedes hacer afirmaciones adicionales aquí
            assertEquals("Preconditions not met.", e.getMessage());
        }
    }

    @Test
    public void TestRefactorFalloTrasformacion() {
        Refactoring refactoring = new ReplaceOrWithInRefactoring();
        String consulta = "SELECT * FROM empleados WHERE estado_civil = 'Soltero' OR estado_civil = 'Casado'";
        try {
            refactoring.refactor(consulta);
            // Si no se lanzó una excepción, la prueba falla
            assertFalse("Se esperaba una excepción, pero no se lanzó.", true);
        } catch (RefactoringException e) {
            // Se lanzó una excepción, puedes hacer afirmaciones adicionales aquí
            assertEquals("Postconditions not met.", e.getMessage());
        }
    }
}
