import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import sqlitegrammar.*;

public class TextVisitor extends SQLiteParserBaseVisitor<String>{

    @Override
    protected String defaultResult() {
		return "";
	}

    @Override
    protected String aggregateResult(String aggregate, String nextResult) {
		return nextResult + aggregate;
	}

    @Override
    public String visitParse(SQLiteParser.ParseContext ctx) { 
        return ctx.getText(); 
    }
}
