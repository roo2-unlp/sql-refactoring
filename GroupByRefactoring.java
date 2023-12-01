
import java.util.ArrayList;
import java.util.Arrays;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import sqlitegrammar.*;

public class GroupByRefactoring extends Refactoring {

    private String stmtParameter, parametersFromSelect = "";
    private Boolean isExistsGroupBy, isParameterContent;
    private ExistsGroupByVisitor existsGroupByVisitor = new ExistsGroupByVisitor();
    private ParameterFromSelectVisitor paramaterFromSelectVisitor = new ParameterFromSelectVisitor();
    private ExistsOrderByVisitor orderByVisitor = new ExistsOrderByVisitor();
    private GetOrderByVisitor getOrderByVisitor = new GetOrderByVisitor();

    public void setStmtParameter(String stmtParemeter) {
        this.stmtParameter = stmtParemeter;
    }   

    @Override
    protected boolean checkPreconditions(String text) {
        SQLiteParser parser = this.createSQLiteParser(text);
        ParseTree tree = parser.parse();
        this.isExistsGroupBy = existsGroupByVisitor.visit(tree);
        this.parametersFromSelect = paramaterFromSelectVisitor.visit(tree);

        this.isParameterContent = isSetterContentValidation(this.parametersFromSelect.split(","),
                this.stmtParameter.split(","));

        if (parser.getNumberOfSyntaxErrors() > 0 || this.isExistsGroupBy || !this.isParameterContent) {
            return false;
        }
        return true;
    }

    @Override
    protected String transform(String text) {
        SQLiteParser parser = this.createSQLiteParser(text);
        ParseTree tree = parser.parse();

        GroupByVisitor groupByVisitor = new GroupByVisitor();

        Boolean isExistsOrderby = orderByVisitor.visit(tree);
        String transformedText = groupByVisitor.visit(tree);
        StringBuilder queryWithGroupByAdded = new StringBuilder();

        return orderByStatementCheck(tree, isExistsOrderby, transformedText, queryWithGroupByAdded);

    }

    @Override
    protected boolean checkPostconditions(String text) {

        SQLiteParser parser = this.createSQLiteParser(text);
        ParseTree tree = parser.parse();

        this.isExistsGroupBy = existsGroupByVisitor.visit(tree);
        this.parametersFromSelect = paramaterFromSelectVisitor.visit(tree);

        if (parser.getNumberOfSyntaxErrors() > 0 || !this.isExistsGroupBy) {
            return false;
        }
        return true;
    }

    private SQLiteParser createSQLiteParser(String text) {
        CharStream charStream = CharStreams.fromString(text);
        SQLiteLexer lexer = new SQLiteLexer(charStream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        return new SQLiteParser(tokens);
    }

    private String orderByStatementCheck(ParseTree tree, Boolean isExistsOrderby, String transformedText,
            StringBuilder queryWithGroupbyAdded) {
        if (isExistsOrderby) {
            queryWithGroupbyAdded.append(transformedText);
            queryWithGroupbyAdded.append(this.stmtParameter);
            String orderByClause = getOrderByVisitor.visit(tree);
            queryWithGroupbyAdded.append(orderByClause + ";");
        } else {
            queryWithGroupbyAdded.append(transformedText);
            queryWithGroupbyAdded.append(this.stmtParameter + ";");
        }
        return queryWithGroupbyAdded.toString();
    }

    private Boolean isSetterContentValidation(String[] parametersSplitted, String[] visitorResultSplitted) {
        for (String parameter : parametersSplitted) {
            for (String visitorParameter : visitorResultSplitted) {
                if (visitorParameter.equals(parameter)) {
                    return true;
                }
            }
        }
        return false;
    }
}