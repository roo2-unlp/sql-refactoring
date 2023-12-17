import static org.junit.Assert.*;
import java.beans.Transient;
import org.junit.Test;

/**
 * Pruebas unitarias para la clase {@link RemoveAliasRefactoring}.
 */
public class RemoveAliasRefactoringTest {

  RemoveAliasRefactoring refactoring;
  String expected;

  /**
   * Prueba el refactoring de una consulta que contiene un alias definido con
   * "AS".
   * Debería devolver la consulta sin el alias.
   *
   * @throws RefactoringException si ocurre un error durante el refactoring.
   */
  @Test
  public void refactorAliasWithAs() throws RefactoringException {
    refactoring = new RemoveAliasRefactoring();
    refactoring.setAlias("tnm");
    expected = "SELECT colum FROM table_name";
    assertEquals(
        expected,
        refactoring.refactor("SELECT colum FROM table_name as tnm"));
  }

  /**
   * Prueba una consulta que no cumple con las precondiciones necesarias para el
   * refactoring.
   * Debería lanzar una excepción {@link RefactoringException}.
   *
   * @throws RefactoringException si ocurre un error durante el refactoring.
   */
  @Test
  public void EqualsQuery() throws RefactoringException {
    refactoring = new RemoveAliasRefactoring();
    refactoring.setAlias("tnm");
    assertThrows(
        RefactoringException.class,
        () -> refactoring.refactor("SELECT * FROM table_name"));
  }

  /**
   * Prueba el refactoring de una consulta que contiene un alias definido con "AS"
   * y referencias.
   * Debería devolver la consulta sin el alias y ajustar las referencias.
   *
   * @throws RefactoringException si ocurre un error durante el refactoring.
   */
  @Test
  public void refactorAliasWithAsAndRef() throws RefactoringException {
    refactoring = new RemoveAliasRefactoring();
    refactoring.setAlias("tnm");
    assertEquals(
        "SELECT * FROM table_name WHERE table_name.codigo = 123",
        refactoring.refactor(
            "SELECT * FROM table_name as tnm WHERE tnm.codigo = 123"));
  }

  /**
   * Prueba el refactoring de una consulta que contiene un alias en una
   * subconsulta.
   * Debería quitar el alias y ajustar las referencias.
   *
   * @throws RefactoringException si ocurre un error durante el refactoring.
   */
  @Test
  public void refactorAliasWithSubquery() throws RefactoringException {
    refactoring = new RemoveAliasRefactoring();
    refactoring.setAlias("tnm2");
    assertEquals(
        "SELECT * FROM table_name as tnm WHERE tnm.codigo = 123 and not in(SELECT table_name2.codigo FROM table_name2 WHERE table_name2.nombre = lautaro)",
        refactoring.refactor(
            "SELECT * FROM table_name as tnm WHERE tnm.codigo = 123 and not in(SELECT tnm2.codigo FROM table_name2 as tnm2 WHERE tnm2.nombre = lautaro)"));
  }

  /**
   * Prueba el refactoring de una consulta que contiene un alias en una cláusula
   * JOIN.
   * Debería quitar el alias elegido y ajustar las referencias.
   *
   * @throws RefactoringException si ocurre un error durante el refactoring.
   */
  @Test
  public void refactorAliasWithJoin() throws RefactoringException {
    refactoring = new RemoveAliasRefactoring();
    refactoring.setAlias("cte");
    assertEquals(
        "SELECT cliente.codigo FROM productos as p JOIN cliente ON cliente.codigo = p.codigo",
        refactoring.refactor(
            "SELECT cte.codigo FROM productos as p JOIN cliente as cte ON cte.codigo = p.codigo"));
  }

  /**
   * Prueba el refactoring de una consulta que contiene un alias de una función.
   * Debería quitar el alias de la función.
   *
   * @throws RefactoringException si ocurre un error durante el refactoring.
   */
  @Test
  public void refactorAliasWithFuntions() throws RefactoringException {
    refactoring = new RemoveAliasRefactoring();
    refactoring.setAlias("max_value");
    assertEquals(
        "SELECT COUNT(*) as count, MAX(column1) FROM table1",
        refactoring.refactor(
            "SELECT COUNT(*) as count, MAX(column1) as max_value FROM table1"));
  }

  /**
   * Prueba el refactoring de una consulta vacía.
   * Debería lanzar una excepción {@link RefactoringException}.
   *
   * @throws RefactoringException si ocurre un error durante el refactoring.
   */
  @Test
  public void refactorEmptyQuery() throws RefactoringException {
    refactoring = new RemoveAliasRefactoring();
    refactoring.setAlias("c");
    assertThrows(RefactoringException.class, () -> refactoring.refactor(" "));
  }

  /**
   * Prueba el refactoring de una consulta que contiene un alias en una columna.
   * Debería quitar el alias de la columna.
   *
   * @throws RefactoringException si ocurre un error durante el refactoring.
   */
  @Test
  public void refactorAliasWithAliasAtColumn() throws RefactoringException {
    refactoring = new RemoveAliasRefactoring();
    refactoring.setAlias("ali");
    assertEquals(
        "SELECT column1 as Alias1, column2 FROM table1",
        refactoring.refactor("SELECT column1 as Alias1, column2 ali FROM table1"));
  }

  /**
   * Prueba el refactoring de una consulta que contiene un alias en una cláusula
   * GROUP BY.
   * Debería quitar el alias y ajustar las referencias.
   *
   * @throws RefactoringException si ocurre un error durante el refactoring.
   */
  @Test
  public void refactorAliasWithGroup() throws RefactoringException {
    refactoring = new RemoveAliasRefactoring();
    refactoring.setAlias("t1");
    assertEquals(
        "SELECT column1, column2 FROM table1 WHERE table1.column1 = table1.column3 GROUP BY table1.column2 ORDER BY table1.column2",
        refactoring.refactor(
            "SELECT column1, column2 FROM table1 as t1 WHERE t1.column1 = t1.column3 GROUP BY t1.column2 ORDER BY t1.column2"));
  }

  /**
   * Prueba una consulta incorrecta que debería lanzar una excepción
   * {@link RefactoringException}.
   */
  @Test
  public void refactorBrokenQuery() {
    refactoring = new RemoveAliasRefactoring();
    refactoring.setAlias("nm");
    assertThrows(
        RefactoringException.class,
        () -> refactoring.refactor("SELECT * FROM name nm as nm WHERE 1=1"));
  }

  /**
   * Prueba general de refactoring con un alias en varias cláusulas.
   * Debería devolver la consulta sin el alias y con las referencias ajustadas.
   *
   * @throws RefactoringException si ocurre un error durante el refactoring.
   */
  @Test
  public void refactorPrueba() throws RefactoringException {
    refactoring = new RemoveAliasRefactoring();
    refactoring.setAlias("t1");
    assertEquals(
        "SELECT table1.column1 FROM table1 WHERE table1.column1 = table1.column3 GROUP BY table1.column2 HAVING table1.column2 > table1.column4 ORDER BY table1.column2",
        refactoring.refactor(
            "SELECT t1.column1 FROM table1 t1 WHERE t1.column1 = t1.column3 GROUP BY t1.column2 HAVING t1.column2 > t1.column4 ORDER BY t1.column2"));
  }
}
