
import java.util.ArrayList;
import java.util.Arrays;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import sqlitegrammar.*;

public class GroupByRefactoring extends Refactoring {

    private String stmtParameter, parametersFromSelect = "";
    private Boolean isExistsGroupBy, isSetterContent;

    public void setStmtParameter(String stmtParemeter) {
        this.stmtParameter = stmtParemeter;
    }

    private SQLiteParser createSQLiteParser(String text) {
        CharStream charStream = CharStreams.fromString(text);
        SQLiteLexer lexer = new SQLiteLexer(charStream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        return new SQLiteParser(tokens);
    }

    @Override
    protected boolean checkPreconditions(String text) {
        SQLiteParser parser = this.createSQLiteParser(text);
        ParseTree tree = parser.parse();

        ExistsGroupByVisitor visitor = new ExistsGroupByVisitor();
        
        ParameterFromSelectVisitor paramaterFromSelectVisitor = new ParameterFromSelectVisitor();
        this.isExistsGroupBy = visitor.visit(tree);
        this.parametersFromSelect = paramaterFromSelectVisitor.visit(tree);       

        this.isSetterContent = isSetterContentValidation(this.parametersFromSelect.split(","),this.stmtParameter.split(","));

        if (parser.getNumberOfSyntaxErrors() > 0 || this.isExistsGroupBy || !this.isSetterContent) {            
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
        System.out.println("AFTER TRANSFORM");
        System.out.println(finalTransformed);
        return finalTransformed.toString();

    }

    @Override
    protected boolean checkPostconditions(String text) {

        SQLiteParser parser = this.createSQLiteParser(text);
        ParseTree tree = parser.parse();

        ExistsGroupByVisitor visitor = new ExistsGroupByVisitor();
        // TODO cambiar el counter por el otro visitor
        ParameterFromSelectVisitor paramaterFromSelectVisitor = new ParameterFromSelectVisitor();

        this.isExistsGroupBy = visitor.visit(tree);
        this.parametersFromSelect = paramaterFromSelectVisitor.visit(tree);
        

        if (parser.getNumberOfSyntaxErrors() > 0 || !this.isExistsGroupBy) {
            return false;
        }

        return true;
    }

    private Boolean isSetterContentValidation(String[] parametersSplitted, String[]visitorResultSplitted) {
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