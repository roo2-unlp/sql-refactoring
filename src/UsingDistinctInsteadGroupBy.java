import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import sqlitegrammar.SQLiteLexer;
import sqlitegrammar.SQLiteParser;

public class UsingDistinctInsteadGroupBy extends Refactoring {
private StringBuilder conEspacios = new StringBuilder();

	private SQLiteParser createSQLiteParser (String text) {
	    CharStream charStream = CharStreams.fromString(text);
	    SQLiteLexer lexer = new SQLiteLexer(charStream);
	    CommonTokenStream tokens = new CommonTokenStream(lexer);
	    SQLiteParser parser = new SQLiteParser(tokens); 
	    parser.setErrorHandler(new BailErrorStrategy()); // usar la estrategia de error Bail
	    return parser;
	}
    
    @Override
	protected boolean checkPreconditions(String text) {
		SQLiteParser parser;
		ParseTree newParseTree;
		try {
			parser = this.createSQLiteParser(text);
			newParseTree = parser.parse();
	        if (parser.getNumberOfSyntaxErrors() > 0) {
	            System.out.println("La consulta tiene errores de sintaxis");
	            return false;
	        }
		}
		catch (ParseCancellationException e) {
		    System.out.println("No es una consulta SQL");
		    return false;
		}
        
        conEspacios.setLength(0);
        ConditionVisitor visitor = new ConditionVisitor();
	    visitor.visit(newParseTree); 
        imprimirNodos(newParseTree);
        System.out.println(arreglarString());
        return visitor.getPrecondicion();    	     		
	}

	@Override
	protected String transform(String text) {
		SQLiteParser parser;
		ParseTree tree;
		try {
			parser = this.createSQLiteParser(text);
			tree = parser.parse();
	        if (parser.getNumberOfSyntaxErrors() > 0){
	            return "La consulta tiene errores de sintaxis";
	        }
	        if (tree.getChildCount() == 1) {
	        	return "Es una consulta vacia";
	        }
		}
		catch (ParseCancellationException e) {
		    return "No es una consulta válida (rta método transform)";
		}
		conEspacios.setLength(0);		
        UsingDistinctInsteadGroupByVisitor visitor = new UsingDistinctInsteadGroupByVisitor();
        visitor.visit(tree);
        imprimirNodos(tree);   
        String transformedText = arreglarString();   
        System.out.println(arreglarString());
        return transformedText;
	}

	@Override
	protected boolean checkPostconditions(String text) {
		SQLiteParser parser;
        ParseTree newParseTree;
        try {
        	parser = this.createSQLiteParser(text);
        	newParseTree = parser.parse();
            if (parser.getNumberOfSyntaxErrors() > 0){
                return false;
            }
        }
		catch (ParseCancellationException e) {
		    return false;
		}
        ConditionVisitor postConditionsVisitor = new ConditionVisitor();
        postConditionsVisitor.visit(newParseTree);
        conEspacios.setLength(0);
        imprimirNodos(newParseTree);
        System.out.println(arreglarString());
        return postConditionsVisitor.getPostcondicion();
	}
	
	private void imprimirNodos(ParseTree tree) {
    	for (int i = 0; i < tree.getChildCount(); i++) {
            ParseTree child = tree.getChild(i);
            // Si el nodo actual es un nodo terminal, agrega su texto al StringBuilder
            if (child instanceof TerminalNode) {
                TerminalNode terminalNode = (TerminalNode) child;
                conEspacios.append(terminalNode.getSymbol().getText());
            } else {
                // Si el nodo no es un nodo terminal, sigue recorriendo recursivamente
                imprimirNodos(child);
            }
            // Agregar un espacio después de cada nodo, excepto después del último
            if (i < tree.getChildCount() - 1) {
                conEspacios.append(" ");
            }
        }
    	
    }
	
	private String arreglarString() {
		return conEspacios.toString().replaceAll("\\s*\\.\\s*", ".").replaceAll(" ; <EOF>", ";");
	}
	
}
	



