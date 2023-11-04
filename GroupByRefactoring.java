
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import sqlitegrammar.*;

public class GroupByRefactoring extends Refactoring{
        
    
    private String preconditionText = null;

    private SQLiteParser createSQLiteParser (String text) {
        CharStream charStream = CharStreams.fromString(text);
        SQLiteLexer lexer = new SQLiteLexer(charStream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        return new SQLiteParser(tokens);
    }

    @Override
    protected boolean checkPreconditions(String text) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'checkPreconditions'");
    }

    @Override
    protected String transform(String text) {
        //Agregar Visitor aca en caso de detectar cual necesitamos tomar de ejemplo el que esta en NullRefactoring
        
        throw new UnsupportedOperationException("Unimplemented method 'transform'");
    }

    @Override
    protected boolean checkPostconditions(String text) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'checkPostconditions'");
    }
    
    
    

}