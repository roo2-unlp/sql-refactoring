import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.beans.Transient;

import org.junit.Test;

public class ReplaceOrWithInRefactoringTest {

    @Test
    public void checkRefactorTrue() throws RefactoringException {
        Refactoring refactoring = new ReplaceOrWithInRefactoring();
        String consulta = "SELECT * FROM empleados WHERE estado_civil = 'Soltero' OR estado_civil = 'Casado'";
        String consultaFinal = "SELECT * FROM empleados WHERE estado_civil IN ('Soltero', 'Casado')";
        assertTrue(refactoring.refactor(consulta).equals(consultaFinal));
    }


    @Test
    public void checkPostConditionsTrue() throws RefactoringException {
        Refactoring refactoring = new ReplaceOrWithInRefactoring();
        String consultaFinal = "SELECT * FROM empleados WHERE estado_civil IN ('Soltero', 'Casado')";
        assertTrue(refactoring.checkPostconditions(consultaFinal));
    }
    @Test
    public void checkPostConditionsFalse() throws RefactoringException {
        Refactoring refactoring = new ReplaceOrWithInRefactoring();
        String consulta = "SELECT * FROM empleados WHERE estado_civil = 'Soltero' OR estado_civil = 'Casado'";
        assertFalse(refactoring.checkPostconditions(consulta));
    }

    @Test
    public void checkPreconditionsTrue() throws RefactoringException {
        Refactoring refactoring = new ReplaceOrWithInRefactoring();
        assertTrue(refactoring.checkPreconditions("SELECT * FROM empleados WHERE estado_civil = 'Soltero' OR estado_civil = 'Casado' OR estado_civil = 'Divorciado';"));
    }

    @Test
    public void checkPreconditionsFalse() throws RefactoringException {
        Refactoring refactoring = new ReplaceOrWithInRefactoring();
        assertFalse(refactoring.checkPreconditions("SELECT * FROM empleados WHERE estado_civil = 'Soltero';"));
    }
    // @Test
    // public void checkPreconditionsIgualdadFalse() throws RefactoringException {
    //     Refactoring refactoring = new ReplaceOrWithInRefactoring();
    //     System.out.println("checkPreconditionsIgualdadFalse");
    //     assertFalse(refactoring.checkPreconditions("SELECT * FROM empleados WHERE edad>=2 OR edad<7"));
    // }
}
