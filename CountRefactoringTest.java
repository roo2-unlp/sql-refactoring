import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.Test;
import java.util.List;

public class CountRefactoringTest {

    @Test
    // (refeferencia query1)
    public void testCountRefactoringWithSimpleQuery() throws RefactoringException {
        Refactoring refactoring = new CountRefactoring("dni");
        String result = refactoring.refactor(
            "SELECT COUNT(*) FROM alumnos WHERE dni>=35000000 AND dni<40000000;"
            );
        assertEquals(
            "SELECT COUNT(dni) FROM alumnos WHERE dni>=35000000 AND dni<40000000;"
            , result
            );
    }

    @Test
    // El refactoring tambien cambia el COUNT en el HAVING, mirar condicional en CountFinderVisitor
    // (referencia query5)
    public void testCountRefactoringWithQueryHAVINGClause() throws RefactoringException {
        Refactoring refactoring = new CountRefactoring("apellido");
        String result = refactoring.refactor(
            "SELECT dni, apellido, nombre, COUNT(*) " +
            "FROM Alumnos a INNER JOIN Examenes e on a.dni = e.dni " +
            "GROUP BY dni, apellido, nombre " +
            "HAVING (COUNT(*) >= 5);"
            );
        assertEquals(
            "SELECT dni, apellido, nombre, COUNT(apellido) " +
            "FROM Alumnos a INNER JOIN Examenes e on a.dni = e.dni " +
            "GROUP BY dni, apellido, nombre " +
            "HAVING (COUNT(*) >= 5);", result
            );
    }

    @Test
    // Pasa test, pero el GROUP BY le falta al menos un campo para agrupar. 
    // (referencia query6)
    public void preConditionsCheck()  {
        boolean withoutStar = false;
        Refactoring refactoring = new CountRefactoring("dni");
        try{
            refactoring.refactor(
                "SELECT COUNT (column)  FROM A WHERE ID > 1 GROUP BY HAVING (age > 17);"
                ); //Need STAR (*)
        }
        catch(Exception e) {
            if (e.getMessage().equals("Preconditions not met."))
                withoutStar = true;
        }
        assertTrue(withoutStar);
    }

    @Test
    // (referencia query8)
    public void testCountRefactoringWithQueryWithSUM() throws RefactoringException {
        Refactoring refactoring = new CountRefactoring("id_producto");
        String result = refactoring.refactor(
            "SELECT COUNT(*) as cant_productos, SUM(precio) as precio_total " + 
            "FROM producto;"
            );
        assertEquals(
            "SELECT COUNT(id_producto) as cant_productos, SUM(precio) as precio_total " +
            "FROM producto;"
            , result
            );
    }
   
    @Test
    public void validatedVariantsofCount()  {
        boolean correctQuery = true;
        Refactoring refactoring = new CountRefactoring("dni");
        List<String> variants = getDifferentCountLayout();
        try{
            for (String query: variants) {
                refactoring.refactor(query);
            }
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
            correctQuery = false;
        }
        assertTrue(correctQuery);
    }

    @Test
    public void testPostConditionsAfterSuccesfulRefactor() {
        boolean withoutStar = true;
        Refactoring refactoring = new CountRefactoring("dni");
        try{
            refactoring.refactor(
            "SELECT dni, apellido, nombre, COUNT(apellido) " +
            "FROM Alumnos a INNER JOIN Examenes e on a.dni = e.dni " +
            "GROUP BY dni, apellido, nombre " +
            "HAVING (COUNT(apellido) >= 5);"
            );
        }
        catch(Exception e) {
            if (e.getMessage().equals("Postconditions not met."))
                withoutStar = false;
        }
        assertTrue(withoutStar);

    }
    
    private List<String> getDifferentCountLayout(){
        return List.of("SELECT CoUnT (*) FROM Liebres;",
                "SELECT CoUNt(*) FROM Condores;",
                "SELECT COUNt(  *  ) from Ratas;",
                "SELECT COUNt (  *) from Leones;");
    }
}