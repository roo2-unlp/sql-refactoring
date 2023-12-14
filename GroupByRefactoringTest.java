import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.beans.Transient;

import org.junit.Test;

public class GroupByRefactoringTest {
    
    // @Test 
    // public void thatDistinctExistsTest() throws RefactoringException {
    //     Refactoring refactoring = new GroupByRefactoring();
    //     //refactoring.refactor("SELECT DISTINCT producto FROM ventas");
    //     // Pregunta si no exite el Distinct en una consulta normal
    //     assertFalse(refactoring.checkPreconditions("SELECT products FROM table_name;"));
    //     // Pregunta si exite el Distinct en una subconsulta
    //     assertTrue(refactoring.checkPreconditions("SELECT * FROM clientes WHERE cliente_id IN (SELECT distinct cliente_id FROM pedidos);;"));
    // }

    // @Test
    // public void transformedTest() throws RefactoringException{
    //     Refactoring refactoring = new GroupByRefactoring();
    //     //refactoring.refactor("SELECT DISTINCT producto , ventas AS ven FROM ventas;");
    //     //assertEquals("SELECT producto FROM ventas GROUP BY producto;", refactoring.transform("SELECT DISTINCT producto FROM ventas;"));
    //     //System.out.println("TEST PRINT: "+refactoring.transform("SELECT DISTINCT producto , ventas AS ven FROM ventas;"));
    //     assertEquals("SELECT producto , ventas AS ven FROM ventas GROUP BY producto , ventas;", refactoring.transform("SELECT DISTINCT producto , ventas AS ven FROM ventas;"));
    // }
    
    // @Test 
    // public void containsAggregationFunctionTest() throws RefactoringException {
    //     Refactoring refactoring = new GroupByRefactoring();
    //     //refactoring.refactor("SELECT DISTINCT producto FROM ventas");
    //     /** Pregunta si tiene una funcion de agregacion */
    //     assertFalse(refactoring.checkPreconditions("SELECT DISTINCT producto, SUM(cantidad) as total_ventas FROM ventas"));
    //     assertTrue(refactoring.checkPreconditions("SELECT DISTINCT producto FROM ventas"));
    // }

    // @Test 
    // public void containsGroupByTest() throws RefactoringException {
    //     Refactoring refactoring = new GroupByRefactoring();
    //     try{
    //         refactoring.refactor("SELECT DISTINCT producto FROM ventas GROUP BY producto;");
    //     }catch(Exception e){};
    //     /** Pregunta si contiene GroupBy */
    //     assertFalse(refactoring.checkPreconditions("SELECT DISTINCT producto FROM ventas GROUP BY producto;"));
    //     assertTrue(refactoring.checkPreconditions("SELECT DISTINCT producto FROM ventas;"));
    // }

    @Test 
    public void groupByrefactorABadQuery()  {
        boolean failure = false;
        Refactoring refactoring = new GroupByRefactoring();
        try{
            refactoring.refactor("FOOBAR * WHERE 1=1;");  
        }
        catch(Exception e) { failure = true; }

        assertTrue(failure);    
    }

    @Test 
    public void containsGroupBy()  {
        boolean failure = false;
        Refactoring refactoring = new GroupByRefactoring();
        try{
            refactoring.refactor("SELECT DISTINCT producto FROM ventas GROUP BY producto;");  
        }
        catch(Exception e) { failure = true; }

        assertTrue(failure);       
    }

    @Test 
    public void missingDistinct()  {
        boolean failure = false;
        Refactoring refactoring = new GroupByRefactoring();
        try{
            refactoring.refactor("SELECT products FROM table_name;");  
        }
        catch(Exception e) { failure = true; }

        assertTrue(failure);       
    }

    @Test 
    public void containsAggregationFunction()  {
        boolean failure = false;
        Refactoring refactoring = new GroupByRefactoring();
        try{
            refactoring.refactor("SELECT DISTINCT producto, SUM(cantidad) as total_ventas FROM ventas");  
        }
        catch(Exception e) { failure = true; }

        assertTrue(failure);       
    }

    @Test
    public void semiColonTest() throws RefactoringException{
        Refactoring refactoring = new GroupByRefactoring();
        String result = refactoring.transform("SELECT DISTINCT producto FROM ventas;");
        assertEquals("SELECT producto FROM ventas GROUP BY producto;", result);

        result = refactoring.transform("SELECT DISTINCT producto FROM ventas");
        assertEquals("SELECT producto FROM ventas GROUP BY producto", result);
    }

    @Test
    public void aliasTest() throws RefactoringException{
        Refactoring refactoring = new GroupByRefactoring();
        String result = refactoring.transform("SELECT DISTINCT producto, ventas AS ven FROM ventas;");
        assertEquals("SELECT producto, ventas AS ven FROM ventas GROUP BY producto, ventas;", result);
    }  
    
    @Test
    public void whereTest() throws RefactoringException{
        Refactoring refactoring = new GroupByRefactoring();
        String result = refactoring.transform("SELECT DISTINCT producto FROM ventas WHERE producto = 'Coca Cola';");
        assertEquals("SELECT producto FROM ventas WHERE producto = 'Coca Cola' GROUP BY producto;", result);
    }
   
    @Test
    public void orderByTest() throws RefactoringException{
        Refactoring refactoring = new GroupByRefactoring();
        String result = refactoring.transform("SELECT DISTINCT categoria FROM Productos ORDER BY categoria;");
        assertEquals("SELECT categoria FROM Productos GROUP BY categoria ORDER BY categoria;", result);
    }

    @Test
    public void limitTest() throws RefactoringException{
        Refactoring refactoring = new GroupByRefactoring();
        String result = refactoring.transform("SELECT DISTINCT departamento FROM Empleados LIMIT 5;");
        assertEquals("SELECT departamento FROM Empleados GROUP BY departamento LIMIT 5;", result);
    }

}
