import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import sqlitegrammar.*;

public class GroupByVisitor extends SQLiteParserBaseVisitor<String>{
    private Boolean containsGroupBy = false;
    
    @Override
    protected String defaultResult() {
		return "";
	}

    @Override
    protected String aggregateResult(String aggregate, String nextResult) {
		return nextResult + aggregate;
	}
    
    @Override
    public String visitSelect_core(SQLiteParser.Select_coreContext ctx) {
        // System.out.println("----------------------------");
        // System.out.println(ctx.getText());
        // if (ctx.getChild(1).getText().toUpperCase().equals("DISTINCT")) {
        //     containsDistinct = true;
        // }
        if (ctx.getText().toUpperCase().contains("GROUPBY")) {
            containsGroupBy = true;
        }
        return ctx.getText();
    }


    public Boolean getContainsGroupBy() {
        return containsGroupBy;
    }
}

