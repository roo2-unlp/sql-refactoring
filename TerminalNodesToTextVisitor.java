import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import sqlitegrammar.*;

public class TerminalNodesToTextVisitor extends SQLiteParserBaseVisitor<String>{

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

}
