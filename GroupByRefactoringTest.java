import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.beans.Transient;

import org.junit.Test;

public class GroupByRefactoringTest {

    @Test 
    public void refactorBadQueryTest()  {
        boolean failure = false;
        Refactoring refactoring = new GroupByRefactoring();
        try{
            refactoring.refactor("FOOBAR * WHERE 1=1;");  
        }
        catch(Exception e) { failure = true; }

        assertTrue(failure);    
    }

    @Test 
    public void containsGroupByTest()  {
        boolean failure = false;
        Refactoring refactoring = new GroupByRefactoring();
        try{
            refactoring.refactor("SELECT DISTINCT producto FROM ventas GROUP BY producto;");  
        }
        catch(Exception e) { failure = true; }

        assertTrue(failure);       
    }

    @Test 
    public void missingDistinctTest()  {
        boolean failure = false;
        Refactoring refactoring = new GroupByRefactoring();
        try{
            refactoring.refactor("SELECT products FROM table_name;");  
        }
        catch(Exception e) { failure = true; }

        assertTrue(failure);       
    }

    @Test 
    public void containsAggregationFunctionTest()  {
        boolean failure = false;
        Refactoring refactoring = new GroupByRefactoring();
        try{
            refactoring.refactor("SELECT DISTINCT producto, SUM(cantidad) as total_ventas FROM ventas");  
        }
        catch(Exception e) { failure = true; }

        assertTrue(failure);       
    }

    @Test 
    public void containsStarTest()  {
        boolean failure = false;
        Refactoring refactoring = new GroupByRefactoring();
        try{
            refactoring.refactor("SELECT DISTINCT * FROM ventas");  
        }
        catch(Exception e) { failure = true; }

        assertTrue(failure);       
    }

    @Test
    public void semicolonTest() throws RefactoringException{
        Refactoring refactoring = new GroupByRefactoring();
        String result = refactoring.refactor("SELECT DISTINCT producto FROM ventas;");
        assertEquals("SELECT producto FROM ventas GROUP BY producto;", result);

        result = refactoring.refactor("SELECT DISTINCT producto FROM ventas");
        assertEquals("SELECT producto FROM ventas GROUP BY producto", result);
    }

    @Test
    public void aliasTest() throws RefactoringException{
        Refactoring refactoring = new GroupByRefactoring();
        String result = refactoring.refactor("SELECT DISTINCT producto, ventas AS ven FROM ventas;");
        assertEquals("SELECT producto, ventas AS ven FROM ventas GROUP BY producto, ventas;", result);
    }  
    
    @Test
    public void whereTest() throws RefactoringException{
        Refactoring refactoring = new GroupByRefactoring();
        String result = refactoring.refactor("SELECT DISTINCT producto FROM ventas WHERE producto = 'Coca Cola';");
        assertEquals("SELECT producto FROM ventas WHERE producto = 'Coca Cola' GROUP BY producto;", result);
    }
   
    @Test
    public void orderByTest() throws RefactoringException{
        Refactoring refactoring = new GroupByRefactoring();
        String result = refactoring.refactor("SELECT DISTINCT categoria FROM Productos ORDER BY categoria;");
        assertEquals("SELECT categoria FROM Productos GROUP BY categoria ORDER BY categoria;", result);
    }

    @Test
    public void limitTest() throws RefactoringException{
        Refactoring refactoring = new GroupByRefactoring();
        String result = refactoring.refactor("SELECT DISTINCT departamento FROM Empleados LIMIT 5;");
        assertEquals("SELECT departamento FROM Empleados GROUP BY departamento LIMIT 5;", result);
    }

}
