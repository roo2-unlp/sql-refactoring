



import static org.junit.Assert.*;

import java.beans.Transient;

// import org.junit.BeforeEach;
import org.junit.Test;


public class RemoveAliasRefactoringTest {
    RemoveAliasRefactoring refactoring;
    String expected;

    // @BeforeEach
    // void setUp() throws Exception {
    //     refactoring = new RemoveAliasRefactoring();
    //     refactoring.setAlias("tnm");
    //     expected = "SELECT * FROM table_name";


    // }

    @Test 
    public void refactorAliasWithAs() throws RefactoringException {
        refactoring = new RemoveAliasRefactoring();
        refactoring.setAlias("tnm");
        expected = "SELECT * FROM table_name";
        assertEquals(expected, refactoring.refactor("SELECT * FROM table_name tnm"));  
 
    } //El refactoring de un query simple(sea con AS o () o /n) el refactor se aplica igual. En este caso la query es con AS y debera devolver el expected.

    @Test 
    public void EqualsQuery() throws RefactoringException {
        refactoring = new RemoveAliasRefactoring(); 
        expected = "SELECT * FROM table_name";
        refactoring.setAlias("tnm");
       assertEquals(expected, refactoring.refactor("SELECT * FROM table_name"));
 
    } //No tiene que pasar las precondiciones
    

   
    @Test 
    public void refactorAliasWithAsAndRef() throws RefactoringException {
        refactoring = new RemoveAliasRefactoring();
        refactoring.setAlias("tnm");
        assertEquals("SELECT * FROM tabla_name where table_name.codigo=123", refactoring.refactor("SELECT * FROM table_name as tnm where tnm.codigo=123"));
      
 
    } //Aca tendra que devolver la query sin el alias y cambiar la referencia a tnm.codigo por table_name.codigo
    

    @Test 
    public void refactorAliasWithSubquery() throws RefactoringException {
       refactoring = new RemoveAliasRefactoring();
       refactoring.setAlias("tnm2");
       assertEquals("SELECT * FROM table_name tnm  where tnm.codigo=123 and not in(select table_name2.codigo from table_name2  where table_name2.nombre=lautaro)"
       , refactoring.refactor("SELECT * FROM table_name tnm where tnm.codigo=123 and not in(select tnm2.codigo from table_name2 as tnm2 where tnm2.nombre=lautaro)"));
       
    } //Aca tendra que quitar el alias y cambiarle la ref de tnm2.nombre a table_name2.nombre y devolver el otro alias normal;
    



   @Test 
    public void refactorAliasWithJoin() throws RefactoringException {
      refactoring = new RemoveAliasRefactoring();
      refactoring.setAlias("c");
      assertEquals("SELECT cliente.codigo,p.nombre FROM producto as p join cliente  on cliente.codigo=p.codigo", refactoring.refactor("SELECT c.codigo,p.nombre FROM productos as p join cliente as c on c.codigo=p.codigo"));
    
 
    } //Aca tendra que quitar el alias elegido y cambiarle la ref de la  tabla que se cruzan ej: si es el alias c
    

    
   @Test 
    public void refactorAliasWithFuntions() throws RefactoringException {
      refactoring = new RemoveAliasRefactoring();
      refactoring.setAlias("max_value");
      assertEquals("SELECT COUNT(*) AS count, MAX(column1) FROM table1", refactoring.refactor("SELECT COUNT(*) AS count, MAX(column1) AS max_value FROM table1"));
    
 
    } //Aca tendra que quitar el alias de la funcion max.
        
    @Test 
    public void refactorEmptyQuery() throws RefactoringException {
      refactoring = new RemoveAliasRefactoring();
      refactoring.setAlias("c");
      assertEquals(" ", refactoring.refactor(" "));
    
 
    } //Casdo de consulta vacia    

    @Test 
    public void refactorAliasWithAliasAtColumn() throws RefactoringException {
      refactoring = new RemoveAliasRefactoring();
      refactoring.setAlias("Alias2");
      assertEquals( "SELECT column1 AS Alias1, column2  FROM table1", refactoring.refactor( "SELECT column1 AS Alias1, column2 AS Alias2 FROM table1"));
    
 
    } //Aca tendra que remover el ALIAS 2 que esta en la columna
    
    @Test 
    public void refactorAliasWithGroupAndOrder() throws RefactoringException {
      refactoring = new RemoveAliasRefactoring();
      refactoring.setAlias("t1");
      assertEquals( "SELECT tabla1.column1 , tabla1.column2  FROM table1   WHERE tabla1.column1 = 1 GROUP BY tabla1.column2 HAVING COUNT(tabla1.column3) > 3 ORDER BY tabla1.column2 DESC", refactoring.refactor("SELECT t1.column1, t1.column2 FROM table1 AS t1 WHERE t1.column1 = 1 GROUP BY t1.column2 HAVING COUNT(t1.column3) > 3 ORDER BY t1.column2 DESC"));
    
 
    } //Aca tendra que remover el alias pasado por parametro y devolver la query cambiando la ref
    

    

    @Test 
    public void refactorBrokenQuery()  {
      refactoring = new RemoveAliasRefactoring();
      refactoring.setAlias("nm");
      assertThrows(RefactoringException.class, () -> refactoring.refactor("SELECT * FROM name nm as nm WHERE 1=1"));  
        

            
    } // deberia dar error de sintaxis



}