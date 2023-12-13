import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import sqlitegrammar.*;

public class TextVisitor extends SQLiteParserBaseVisitor<String>{
    
    private String transformedText;

    public TextVisitor() {
        transformedText = "";
    }

    @Override
    protected String defaultResult() {
		return "";
	}

    @Override
    protected String aggregateResult(String aggregate, String nextResult) {
		return aggregate + nextResult;
	}

    @Override 
	public String visitSelect_core(SQLiteParser.Select_coreContext ctx) { 
        System.out.println("TextVisitor:"+ctx.getText());
        for (int i = 0; i < ctx.getChildCount(); i++) {
            transformedText = transformedText + ctx.getChild(i).getText() + " ";
        }
        
        return transformedText;
	}

    public String getTransformedText() {
        return this.transformedText;
    }
}
