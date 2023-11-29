import java.util.List;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import sqlitegrammar.*;

public class LimitWithOrderBy extends Refactoring{
    private String preconditionText = null;
    private int limit=10;

    public void setLimit(int limnit){
        this.limit = limit;
    }
    // queda igual
    private SQLiteParser createSQLiteParser (String text) {
        CharStream charStream = CharStreams.fromString(text);
        SQLiteLexer lexer = new SQLiteLexer(charStream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        return new SQLiteParser(tokens);
    }

    protected boolean checkPreconditions(String text) {
        SQLiteParser parser = this.createSQLiteParser(text);
        ParseTree newParseTree = parser.parse();

        ExistsOrderByVisitor existsOrderByVisitor = new ExistsOrderByVisitor();
        Boolean isExistOrderBy = existsOrderByVisitor.visit(tree);
        // aca crear un visitor para las precondiciones y despues crear un if
        // donde compruebe todos los metodos, si alguno retorna falso, retorno falso.
        
        if (parser.getNumberOfSyntaxErrors() > 0 || !isExistOrderBy ) {
            return false;
        }

        return true;
        //preconditionText = newParseTree.getText();
    }
 
    @Override
    protected String transform(String text) {
        SQLiteParser parser = this.createSQLiteParser(text);
        ParseTree tree = parser.parse();
        LimitWithOrderByVisitor visitor = new LimitWithOrderByVisitor();

        String transformedText = visitor.visit(tree);

        return transformedText.split(" ").length;   
    }

    // verifica que la consulta despues de la transformacion tenga LIMIT
    protected boolean checkPostconditions(String text) {
    }
}
