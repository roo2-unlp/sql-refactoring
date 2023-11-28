import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import sqlitegrammar.*;

public class SelectDistinctVisitor extends SQLiteParserBaseVisitor<String>{
    @Override
    protected String defaultResult() {
		return "";
	}

    @Override
    protected String aggregateResult(String aggregate, String nextResult) {
		return nextResult + aggregate;
	}
    // override visitselect_core
    @Override
    public String visitSelect_core(SQLiteParser.Select_coreContext ctx) {
       
        //System.out.println("Print del visitor: ctx.getText()");
        //System.out.println(ctx.getText());
        return ctx.getText();
    }
}
