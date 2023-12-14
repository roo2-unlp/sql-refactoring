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
    public String visitTerminal(TerminalNode node) {
        return node.getText() + " ";
    }

    
    // @Override 
	// public String visitSelect_core(SQLiteParser.Select_coreContext ctx) { 
    //     System.out.println("TextVisitor:"+ctx.getText());
    //     for (int i = 0; i < ctx.getChildCount(); i++) {
    //         if (ctx.getChild(i) instanceof SQLiteParser.Result_columnContext) {
    //             transformedText = transformedText + visitResult_column((SQLiteParser.Result_columnContext) ctx.getChild(i));
    //         } else {
    //             System.out.println(ctx.getChild(i).getClass());
    //             transformedText = transformedText + ctx.getChild(i).getText() + " ";
    //         }
    //     }
        
    //     return transformedText;
	// }

    // @Override 
    // public String visitResult_column(SQLiteParser.Result_columnContext ctx) {
    //     String transformedText = "";

    //     for (int i = 0; i < ctx.getChildCount(); i++) {
    //         transformedText = transformedText + ctx.getChild(i).getText() + " ";
    //     }
    //     return transformedText;
    // }

    //  @Override 
    // public String visitExpr(SQLiteParser.ExprContext ctx) {
    //     String transformedText = "";

    //     for (int i = 0; i < ctx.getChildCount(); i++) {
    //         transformedText = transformedText + ctx.getChild(i).getText() + " ";
    //     }
    //     return transformedText;
    // }
    

    // public String getTransformedText() {
    //     return this.transformedText;
    // }
}
