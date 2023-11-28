import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.beans.Transient;

import org.junit.Test;

public class GroupByRefactoringTest {
    
    @Test 
    public void thatDistinctExistsTest() throws RefactoringException {
        String result;
        Refactoring refactoring = new GroupByRefactoring();
        /** Pregunta si no exite el Distinct en una consulta normal */
        assertFalse(refactoring.checkPreconditions("SELECT products FROM table_name;"));
        /** Pregunta si exite el Distinct en una subconsulta */
        assertTrue(refactoring.checkPreconditions("SELECT * FROM clientes WHERE cliente_id IN (SELECT distinct cliente_id FROM pedidos);;"));

    }
    
    @Test 
    public void containsAggregationFunctionTest() throws RefactoringException {
        String result;
        Refactoring refactoring = new GroupByRefactoring();
        /** Pregunta si tiene una funcion de agregacion */
        assertFalse(refactoring.checkPreconditions("SELECT DISTINCT producto, SUM(cantidad) as total_ventas FROM ventas"));
    }

    @Test 
    public void containsGroupByTest() throws RefactoringException {
        String result;
        Refactoring refactoring = new GroupByRefactoring();
        /** Pregunta si contiene GroupBy */
        assertFalse(refactoring.checkPreconditions("SELECT DISTINCT producto, SUM(cantidad) as total_ventas FROM ventas GROUP BY producto;"));
    }

}
