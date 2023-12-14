import org.antlr.v4.runtime.CommonToken;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.antlr.v4.runtime.tree.TerminalNodeImpl;

public class SeparateTokensVisitor extends SQLiteParserBaseVisitor<String> {
    private StringBuilder separatedWords;
    private boolean esEspecial = false;

    public SeparateTokensVisitor() {
        super();
        this.separatedWords = new StringBuilder();
    }
    
    @Override
    public String visitTerminal(TerminalNode node) {
        String text = node.getText();
        if (!text.equals("<EOF>") )
            appendSeparatedWords(text);

        return super.visitTerminal(node);
    }
    


    public StringBuilder getSeparatedWords() {
		return separatedWords;
	}
/*
	   private void appendSeparatedWords(String text) {
        if (!text.equals(",") && !text.equals(";")) {
            if (separatedWords.length() > 0 && !text.equals(".")) {
                if (!this.esEspecial || !text.equals(")")) {
                    separatedWords.append(" ");
                } else {
                    this.esEspecial = false;
                }
                separatedWords.append(text);
            } else {
                if (text.equals(".")) {
                    this.esEspecial = true;
                    separatedWords.append(text);
                } else {
                    separatedWords.append(text);
                }
            }
        } else
            separatedWords.append(text);
    }
    */
    private void appendSeparatedWords(String text) {
    	
    	if (separatedWords.length() > 0) {
    		if (text.equals(".")) {
    			this.esEspecial = true;
    			separatedWords.append(text);
    		}
    		else {
        		if (this.esEspecial) {
        			this.esEspecial = false;
        			separatedWords.append(text);
        		}
        		else {
        			if (text.equals(";") || text.equals(",") || text.equals(")")) {
            			separatedWords.append(text);
            		}
        			else 
        				separatedWords.append(" " + text);
        		}
    		}
    	}
    	else {
    		separatedWords.append(text);
    	}
    }
    
    
    @Override
    protected String defaultResult() {
        return "";
    }

    @Override
    protected String aggregateResult(String aggregate, String nextResult) {
        if (aggregate == null) {
            return nextResult; // Si el resultado acumulado es null, simplemente devuelve el resultado del nodo
                               // hijo
        } else {
            return aggregate + nextResult; // Concatena los resultados
        }
    }
}