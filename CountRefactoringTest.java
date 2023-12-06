import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import org.junit.Test;
import java.util.List;

public class CountRefactoringTest {

    
    private Refactoring refactoring;

    @Test
    // (Referencia query1)
    public void testCountRefactoringWithSimpleQuery() throws RefactoringException {
        refactoring = new CountRefactoring("dni");
        String result = refactoring.refactor(
            "SELECT COUNT(*) FROM alumnos WHERE dni>=35000000 AND dni<40000000;"
            );
        assertEquals(
            "SELECT COUNT(dni) FROM alumnos WHERE dni>=35000000 AND dni<40000000;"
            , result
            );
    }

    @Test
    // (Referencia query5)
    public void testCountRefactoringWithQueryHAVINGClause() throws RefactoringException {
        refactoring = new CountRefactoring("apellido");
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
    public void checkPreConditionsWithCountOnlyInHaving() {
        refactoring = new CountRefactoring("apellido");
        boolean needCountInSelect = false;
        try{
            refactoring.refactor(
                    "SELECT dni, apellido, nombre " +
                    "FROM Alumnos a INNER JOIN Examenes e on a.dni = e.dni " +
                    "GROUP BY dni, apellido, nombre " +
                    "HAVING (COUNT(*) >= 5);"
            );      //No matter this operation
        }
        catch(Exception e) {
            if (e.getMessage().equals("Preconditions not met."))
                needCountInSelect = true;
        }
        assertTrue(needCountInSelect);
    }

    @Test
    public void preConditionsCheck()  {
        boolean needStar = false;
        refactoring = new CountRefactoring("dni");
        try{
            refactoring.refactor(
                "SELECT COUNT(column), SUM(*) FROM A WHERE ID > 1 GROUP BY column HAVING (age > 17);"
                ); //Need STAR in count node (*)
        }
        catch(Exception e) {
            if (e.getMessage().equals("Preconditions not met."))
                needStar = true;
        }
        assertTrue(needStar);
    }

    @Test
    public void postConditionsCheck() {
        boolean withoutStar = true;
        refactoring = new CountRefactoring("dni");

        withoutStar = refactoring.checkPostconditions("SELECT COUNT(*) FROM table_1;");
        assertFalse(withoutStar);

        withoutStar = refactoring.checkPostconditions("SELECT COUNT(dni) FROM table_1;");
        assertTrue(withoutStar);
    }

    @Test
    // (referencia query8)
    public void testCountRefactoringWithQueryWithSUM() throws RefactoringException {
        refactoring = new CountRefactoring("id_producto");
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
    // (referencia query8)
    public void testCountRefactoringWithQueryWithAVG() throws RefactoringException {
        refactoring = new CountRefactoring("id_producto");
        String result = refactoring.refactor(
                "SELECT COUNT(*) as cant_productos, AVG(*) as precio_total " +
                        "FROM producto;"
        );
        assertEquals(
                "SELECT COUNT(id_producto) as cant_productos, AVG(*) as precio_total " +
                        "FROM producto;"
                , result
        );
    }

    @Test
    // (referencia query8)
    public void testCountRefactoringWithQueryWithMIN() throws RefactoringException {
        refactoring = new CountRefactoring("id_producto");
        String result = refactoring.refactor(
                "SELECT COUNT(*) as cant_productos, MIN(*) as precio_total " +
                        "FROM producto;"
        );
        assertEquals(
                "SELECT COUNT(id_producto) as cant_productos, MIN(*) as precio_total " +
                        "FROM producto;"
                , result
        );
    }

    @Test
    public void testCountRefactoringWithQueryWithMAX() throws RefactoringException {
        refactoring = new CountRefactoring("id_producto");
        String result = refactoring.refactor(
                "SELECT COUNT(*) as cant_productos, MAX(*) as precio_total " +
                        "FROM producto;"
        );
        assertEquals(
                "SELECT COUNT(id_producto) as cant_productos, MAX(*) as precio_total " +
                        "FROM producto;"
                , result
        );
    }

    @Test
    //Valido cooperación entre partes del refactoring y valido diversas disposiciones de op. count posibles
    public void validatedVariantsofCount()  {
        boolean correctQuery = true;
        refactoring = new CountRefactoring("dni");
        List<String> variants = getDifferentCountSyntax();
        try{
            for (String query: variants) {
                refactoring.refactor(query);
            }
        }
        catch(Exception e) {
            correctQuery = false;
        }
        assertTrue(correctQuery);
    }
    
    private List<String> getDifferentCountSyntax(){
        return List.of("SELECT CoUnT (*) FROM Liebres where age > 1;",
                "SELECT CoUNt(*) FROM Condores;",
                "SELECT COUNt(  *  ) from Ratas;",
                "SELECT count (  *) from Leones;");
    }
}