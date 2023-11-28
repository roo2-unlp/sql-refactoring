import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class UsingDistinctInsteadGroupByTest {

	private UsingDistinctInsteadGroupBy refactoring;
	private String consultaRefactorizable1;
	private String consultaRefactorizable2;
	private String consultaRefactorizable3;
	private String consultaRefactorizable4;
	private String consultaNoRefactorizable1;
	private String consultaNoRefactorizable2;	
	private String consultaRefactorizada1;
	private String consultaRefactorizada2;
	private String consultaRefactorizada3;
	private String consultaRefactorizada4;
	private String consultaVacia;
	private String consultaNoSql;
	private String quitarSaltos = "\r\n";
	
    @Before
    public void setUp () throws Exception {
    	refactoring = new UsingDistinctInsteadGroupBy();
    	
    	consultaRefactorizable1 = "SELECT category FROM products GROUP BY category;";
    	consultaRefactorizable2 = "SELECT d.nombre AS nombre_departamento , e.nombre AS nombre_empleado\r\n"
				+ "FROM empleados AS e\r\n"
				+ "JOIN asignaciones AS a ON e.id = a.id_empleado\r\n"
				+ "JOIN departamentos AS d ON a.id_departamento = d.id\r\n"
				+ "GROUP BY d.id, e.id;";
    	consultaRefactorizable3 = "SELECT department, employee_id\r\n "
    			+ "FROM employees\r\n "
    			+ "WHERE salary > 50000 AND (employee_id > 100 OR employee_id < 50)\r\n "
    			+ "GROUP BY department, employee_id;";
    	
    	consultaRefactorizable4 = " SELECT titulo \r\n"
    			+ "FROM publicaciones\r\n "
    			+ "WHERE nivel_id IN (\r\n "
    			+ "   SELECT id FROM niveles WHERE nombre LIKE \"%facil%\"\r\n "
    			+ ") AND usuario_id IN (\r\n"
    			+ "  SELECT id FROM usuarios WHERE nombre LIKE \"%Eileen%\"\r\n "
    			+ ") AND titulo LIKE \"%css%\"\r\n "
    			+ "GROUP BY nivel_id, usuario_id;";
    	
    	consultaVacia = " ";
    	
    	consultaNoSql = "hoy va a llover";
    	 	
    	consultaNoRefactorizable1 = "SELECT category, COUNT (product) FROM products GROUP BY category;";
    	consultaNoRefactorizable2 = "SELECT producto, SUM(cantidad_vendida) AS total_vendido FROM ventas GROUP BY producto;";
    	
    	consultaRefactorizada1 = "SELECT DISTINCT category FROM products;";
    	consultaRefactorizada2 = "SELECT DISTINCT d.nombre AS nombre_departamento , e.nombre AS nombre_empleado\r\n "
				+ "FROM empleados AS e\r\n "
				+ "JOIN asignaciones AS a ON e.id = a.id_empleado\r\n "
				+ "JOIN departamentos AS d ON a.id_departamento = d.id;";
    	
    	consultaRefactorizada3 = "SELECT DISTINCT department , employee_id\r\n "
    			+ "FROM employees\r\n "
    			+ "WHERE salary > 50000 AND ( employee_id > 100 OR employee_id < 50 );";

    	consultaRefactorizada4 = "SELECT DISTINCT titulo \r\n"
    			+ "FROM publicaciones \r\n"
    			+ "WHERE nivel_id IN ( \r\n"
    			+ "SELECT DISTINCT id FROM niveles WHERE nombre LIKE \"%facil%\" \r\n"
    			+ ") AND usuario_id IN ( \r\n"
    			+ "SELECT DISTINCT id FROM usuarios WHERE nombre LIKE \"%Eileen%\" \r\n"
    			+ ") AND titulo LIKE \"%css%\";";
    }

    /* test de unidad para comprobar el uso del método que chequea las pre-condiciones 
     * de la clase UsingDistinctInsteadGroupBy.java, en este método comprueba los casos 
     * exitosos de las precondiciones de un String mandado por parámetro 
     * las cuales serian que contenga un token del tipo GROUP y que no tenga funciones
     * de agregacion. Este test en particluar conprueba los casos en que si se cumple */
    @Test
    public void testCheckPreconditions() {
    	System.out.println("Chequeo exitoso de pre-condicíon de las siguientes consultas:");
        assertTrue(refactoring.checkPreconditions(consultaRefactorizable1));
        assertTrue(refactoring.checkPreconditions(consultaRefactorizable2));
        assertTrue(refactoring.checkPreconditions(consultaRefactorizable3));
        assertTrue(refactoring.checkPreconditions(consultaRefactorizable4));
    }
    
    /* test de unidad para comprobar el uso del método que chequea las pre-condiciones 
     * de la clase UsingDistinctInsteadGroupBy.java, en este método comprueba los casos 
     * fallidos de las precondiciones de un String mandado por parámetro 
     * las cuales serian que contenga un token del tipo GROUP y que no tenga funciones
     * de agregacion. Este test en particluar conprueba los casos en que no se cumple */
    @Test
    public void testCheckPreconditionsFAILURE() {
    	System.out.println("Chequeo fallido de pre-condicíon de las siguientes consultas:");
        assertFalse(refactoring.checkPreconditions(consultaNoRefactorizable1));
        assertFalse(refactoring.checkPreconditions(consultaNoRefactorizable2));
        assertFalse(refactoring.checkPreconditions(consultaVacia));
        assertFalse(refactoring.checkPreconditions(consultaNoSql));
    }
    
    /* test de unidad para comprobar el uso del método transform que hace efectivo el refactoring sobre
     * un string enviado como parametro que cumple las pre-condiciones (que sea una consulta sql válida, 
     * que tengo GROUP y que no tenga funciones de agregación) de la clase UsingDistinctInsteadGroupBy.java,
     *  en este test en particular se analizan string que sí cumplen las pre-condiciones  */
    @Test
    public void testRefactoring() { 

    	assertEquals(consultaRefactorizada1.replaceAll(quitarSaltos, ""), refactoring.transform(consultaRefactorizable1));	
    	assertEquals(consultaRefactorizada2.replaceAll(quitarSaltos, ""), refactoring.transform(consultaRefactorizable2)); 
    	assertEquals(consultaRefactorizada3.replaceAll(quitarSaltos, ""), refactoring.transform(consultaRefactorizable3));
    	assertEquals(consultaRefactorizada4.replaceAll(quitarSaltos, ""), refactoring.transform(consultaRefactorizable4));
    }
    
    /* test de unidad para comprobar el uso del método transform que hace efectivo el refactoring sobre
     * un string que cumple las pre-condiciones (que sea una consulta sql válida, que tengo GROUP y que no
     * tenga funciones de agregación) de la clase UsingDistinctInsteadGroupBy.java, en este test en particular
     * se analizan string que sí cumplen las pre-condiciones  */
    @Test
    public void testRefactoringFAILURE() { 
    	assertEquals("No es una consulta válida (rta método transform)", refactoring.transform(consultaNoSql));
    	assertEquals("Es una consulta vacia", refactoring.transform(consultaVacia));
    }
    
    /* test de unidad para comprobar el uso del método que chequea las post-condiciones 
     * de la clase UsingDistinctInsteadGroupBy.java, en este método comprueba los casos 
     * exitosos de las post-condiciones de un String mandado por parámetro 
     * las cuales serian que no contenga un token del tipo GROUP, que no tenga funciones
     * de agregacion y que sea una consulta sql válida. 
     * Este test en particluar conprueba los casos en que sí se cumple */
    @Test
    public void testCheckPostConditions() {
    	System.out.println("Chequeo exitoso de post-condicíon de las siguientes consultas:");
    	assertTrue(refactoring.checkPostconditions(consultaRefactorizada1.replaceAll(quitarSaltos, "")));
    	assertTrue(refactoring.checkPostconditions(consultaRefactorizada2.replaceAll(quitarSaltos, "")));
    	assertTrue(refactoring.checkPostconditions(consultaRefactorizada3.replaceAll(quitarSaltos, "")));
    	assertTrue(refactoring.checkPostconditions(consultaRefactorizada4.replaceAll(quitarSaltos, "")));
    }
    
    /* test de unidad para comprobar el uso del método que chequea las post-condiciones 
     * de la clase UsingDistinctInsteadGroupBy.java, en este método comprueba los casos 
     * fallidos de las post-condiciones de un String mandado por parámetro 
     * las cuales serian que no contenga un token del tipo GROUP, que no tenga funciones
     * de agregacion y que sea una consulta sql válida. 
     * Este test en particluar conprueba los casos en que no se cumple */
    @Test
    public void testCheckPostConditionsFAILURE() {
    	System.out.println("Chequeo fallido de post-condicíon de las siguientes consultas:");
    	assertFalse(refactoring.checkPostconditions(consultaNoRefactorizable1));
    	assertFalse(refactoring.checkPostconditions(consultaNoRefactorizable2));
    }
    
    /* test de unidad para comprobar el método refactor de la clase Refactoring.java, en la cual se
     * llama a los metodos de pre y post condición, asi como el método tranform de la clase
     * UsingDistinctInsteadGroupBy.java segun se cumplan las condiciones para poder ejecutarlo.
     * Este test en partiuclar comprueba casos exitosos.*/
    @Test
    public void testRefactor() throws RefactoringException {
    	System.out.println("Chequeo exitoso del metodo refactor:");
    	assertEquals(consultaRefactorizada1.replaceAll(quitarSaltos, ""), refactoring.refactor(consultaRefactorizable1));	
    	assertEquals(consultaRefactorizada2.replaceAll(quitarSaltos, ""), refactoring.refactor(consultaRefactorizable2));	
    	assertEquals(consultaRefactorizada3.replaceAll(quitarSaltos, ""), refactoring.refactor(consultaRefactorizable3));
    	assertEquals(consultaRefactorizada4.replaceAll(quitarSaltos, ""), refactoring.refactor(consultaRefactorizable4));
        
    }
    
    /* test de unidad para comprobar el método refactor de la clase Refactoring.java, en la cual se
     * llama a los metodos de pre y post condición, asi como el método tranform de la clase
     * UsingDistinctInsteadGroupBy.java segun se cumplan las condiciones para poder ejecutarlo.
     * Este test en partiuclar comprueba casos fallidos.*/
    @Test
    public void testRefactorFAILURE() throws RefactoringException {	
    	System.out.println("Chequeo fallido del metodo refactor:");
    	RefactoringException exception1 = assertThrows(RefactoringException.class,
                () -> refactoring.refactor(consultaNoRefactorizable1));
        assertEquals("Preconditions not met.", exception1.getMessage());

        RefactoringException exception2 = assertThrows(RefactoringException.class,
                () -> refactoring.refactor(consultaNoRefactorizable2));
        assertEquals("Preconditions not met.", exception2.getMessage());
        
    }
}
