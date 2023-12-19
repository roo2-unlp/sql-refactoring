import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Before;
import org.junit.Test;

public class UsingDistinctInsteadGroupByTest {

	private UsingDistinctInsteadGroupBy refactoring;
	private String consultaRefactorizable1;
	private String consultaRefactorizable2;
	private String consultaRefactorizable3;
	private String consultaNoRefactorizable1;
	private String consultaNoRefactorizable2;
	private String consultaNoRefactorizable3;
	private String consultaRefactorizada1;
	private String consultaRefactorizada2;
	private String consultaRefactorizada3;
	private String consultaVacia;
	private String consultaNoSql;
	private String consultaRefactorizable4;
	private String consultaRefactorizada4;
	private String c5NoRefactorizable;
	private String consultaConAsterisco;
	private String quitarSaltos = "\r\n";
	
    @Before
    public void setUp () throws Exception {
    	refactoring = new UsingDistinctInsteadGroupBy();
    	
    	consultaRefactorizable1 = "SELECT category FROM products GROUP BY category;";
    	
    	consultaRefactorizable2 = "SELECT d.nombre AS nombre_departamento, e.nombre AS nombre_empleado\r\n"
    			+ "FROM empleados AS e\r\n"
    			+ "JOIN asignaciones AS a ON e.id = a.id_empleado\r\n"
    			+ "JOIN departamentos AS d ON a.id_departamento = d.id\r\n"
    			+ "GROUP BY d.nombre, e.nombre;";
    	
    	consultaRefactorizable3 = "SELECT department, employee_id\r\n "
    			+ "FROM employees\r\n "
    			+ "WHERE salary > 50000 AND (employee_id > 100 OR employee_id < 50)\r\n "
    			+ "GROUP BY department, employee_id;";
    	
    	consultaRefactorizable4 = "SELECT d.department AS dd , e.employee_id AS tt\r\n "
    			+ "FROM employees\r\n "
    			+ "WHERE salary > 20000 AND (employee_id > 100 OR employee_id < 50)\r\n "
    			+ "GROUP BY d.department, d.employee_id;";
    	
    	//no se puede refactorizar consultas vacias
    	consultaVacia = " ";
    	
    	//no se puede refactorizar consultas que no sean SQLite validas
    	consultaNoSql = "hoy va a llover";
    	 	
    	//no se puede refactorizar consultas con funciones de agregacion
    	consultaNoRefactorizable2 = "SELECT producto, SUM(cantidad_vendida) AS total_vendido FROM ventas GROUP BY producto;";
    	
    	// esta consulta no coinciden las columnas del SELECT con las del GROUP BY
    	consultaNoRefactorizable3 = "SELECT nombre, apellido, edad FROM personas GROUP BY nombre;";
    	
    	consultaRefactorizada2 = "SELECT DISTINCT d.nombre AS nombre_departamento , e.nombre AS nombre_empleado\r\n "
    			+ "FROM empleados AS e\r\n "
    			+ "JOIN asignaciones AS a ON e.id = a.id_empleado\r\n "
    			+ "JOIN departamentos AS d ON a.id_departamento = d.id;";
    	
    	consultaRefactorizada3 = "SELECT DISTINCT department , employee_id\r\n "
    			+ "FROM employees\r\n "
    			+ "WHERE salary > 50000 AND ( employee_id > 100 OR employee_id < 50 );";
    	
    	consultaRefactorizada4 = "SELECT DISTINCT d.department AS dd , e.employee_id AS tt\r\n "
    			+ "FROM employees\r\n "
    			+ "WHERE salary > 20000 AND ( employee_id > 100 OR employee_id < 50 );";
    	
    	c5NoRefactorizable = "SELECT COUNT(*) as total_pedidos\r\n "
    			+ "FROM pedidos\r\n "
    			+ "GROUP BY cliente\r\n "
    			+ "HAVING COUNT(*) > 2;";
    	
    	consultaConAsterisco = "SELECT * FROM empleados GROUP BY id, nombre, apellido, edad;";
    }
    
    /* test de unidad para comprobar el m�todo refactor de la clase Refactoring.java, en la cual se
     * llama a los metodos de pre y post condici�n, asi como el m�todo tranform de la clase
     * UsingDistinctInsteadGroupBy.java segun se cumplan las condiciones para poder ejecutarlo.
     * Este test en partiuclar comprueba casos exitosos.*/
    @Test
    public void testRefactor() throws RefactoringException {
    	System.out.println("Chequeo exitoso del metodo refactor:"); 	
    	assertEquals(consultaRefactorizada2.replaceAll(quitarSaltos, ""), refactoring.refactor(consultaRefactorizable2));	
    	assertEquals(consultaRefactorizada3.replaceAll(quitarSaltos, ""), refactoring.refactor(consultaRefactorizable3));  
    	assertEquals(consultaRefactorizada4.replaceAll(quitarSaltos, ""), refactoring.refactor(consultaRefactorizable4));
    }
    
    /* test de unidad para comprobar el m�todo refactor de la clase Refactoring.java, en la cual se
     * llama a los metodos de pre y post condici�n, asi como el m�todo tranform de la clase
     * UsingDistinctInsteadGroupBy.java segun se cumplan las condiciones para poder ejecutarlo.
     * Este test en particular comprueba casos fallidos.*/
    @Test
    public void testRefactorFAILURE() throws RefactoringException {	
    	System.out.println("Chequeo fallido del metodo refactor:");

        RefactoringException exception2 = assertThrows(RefactoringException.class,
                () -> refactoring.refactor(consultaNoRefactorizable2));
        assertEquals("Preconditions not met.", exception2.getMessage());
        
        RefactoringException exception3 = assertThrows(RefactoringException.class,
                () -> refactoring.refactor(consultaVacia));
        assertEquals("Preconditions not met.", exception3.getMessage());
        
        RefactoringException exception4 = assertThrows(RefactoringException.class,
                () -> refactoring.refactor(consultaNoSql));
        assertEquals("Preconditions not met.", exception4.getMessage());
        
        RefactoringException exception5 = assertThrows(RefactoringException.class,
                () -> refactoring.refactor(consultaNoRefactorizable3));
        assertEquals("Preconditions not met.", exception5.getMessage());
        
        RefactoringException exception6 = assertThrows(RefactoringException.class,
                () -> refactoring.refactor(c5NoRefactorizable));
        assertEquals("Preconditions not met.", exception6.getMessage());
        
        RefactoringException exception7 = assertThrows(RefactoringException.class,
                () -> refactoring.refactor(consultaConAsterisco));
        assertEquals("Preconditions not met.", exception6.getMessage());  
    }
}