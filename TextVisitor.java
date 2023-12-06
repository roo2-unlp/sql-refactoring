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
        transformedText = nextResult + " " + transformedText;
		return nextResult + aggregate;
	}

    @Override
    public String visitParse(SQLiteParser.ParseContext ctx) { 
        return ctx.getText(); 
    }

    public String getTransformedText() {
        return this.transformedText;
    }
}
