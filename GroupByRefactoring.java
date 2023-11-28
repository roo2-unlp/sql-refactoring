
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import sqlitegrammar.*;

public class GroupByRefactoring extends Refactoring {

    private String stmtParameter;

    public void setStmtParameter(String stmtParemeter) {
        this.stmtParameter = stmtParemeter;
    }

    private SQLiteParser createSQLiteParser(String text) {
        CharStream charStream = CharStreams.fromString(text);
        SQLiteLexer lexer = new SQLiteLexer(charStream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        return new SQLiteParser(tokens);
    }

    // TODO mejorar las precondiciones para validar si existe el group by,validar
    // cantidad de parametros que tenemos
    @Override
    protected boolean checkPreconditions(String text) {
        SQLiteParser parser = this.createSQLiteParser(text);
        ParseTree tree = parser.parse();

        ExistsGroupByVisitor visitor = new ExistsGroupByVisitor();
        CounterParameterVisitor counterVisitor = new CounterParameterVisitor();
        Boolean isExistGroupBy = visitor.visit(tree);
        int parameterSize = counterVisitor.visit(tree);
        Boolean sameSizeOfParameter = parameterSize == stmtParameter.split(",").length;      

        if (parser.getNumberOfSyntaxErrors() > 0 || isExistGroupBy || !sameSizeOfParameter) {
            return false;
        }
        return true;
    }

    @Override
    protected String transform(String text) {
        SQLiteParser parser = this.createSQLiteParser(text);
        ParseTree tree = parser.parse();
        GroupByVisitor visitor = new GroupByVisitor();

        String transformedText = visitor.visit(tree);
        StringBuilder finalTransformed = new StringBuilder();

        finalTransformed.append(transformedText);
        finalTransformed.append(this.stmtParameter + ";");

        return finalTransformed.toString();

    }

    @Override
    protected boolean checkPostconditions(String text) {

        SQLiteParser parser = this.createSQLiteParser(text);
        ParseTree tree = parser.parse();

        ExistsGroupByVisitor visitor = new ExistsGroupByVisitor();
        CounterParameterVisitor counterVisitor = new CounterParameterVisitor();

        Boolean isExistGroupBy = visitor.visit(tree);
        int parameterSize = counterVisitor.visit(tree);
        Boolean sameSizeOfParameter = parameterSize == stmtParameter.split(",").length;

        // TODO agregar post conditions luego de la transformacion
        if (parser.getNumberOfSyntaxErrors() > 0 || !isExistGroupBy || !sameSizeOfParameter) {
            return false;
        }

        return true;
    }
}