import java.util.Arrays;

import org.antlr.v4.gui.TreeViewer;
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

	/* Metodo para crear el arbol SQLiteParse con la consulta SQLite ingresada como un string
	 * utilizando la gramatica provista por la catedra */
	private SQLiteParser createSQLiteParser (String text) {
		//se convierte el texto en un flujo de caracteres
	    CharStream charStream = CharStreams.fromString(text);
	    
	    //se divide el flujo de caracteres en tokens creando el lexer, cada token es
	    //una unidad lexica
	    SQLiteLexer lexer = new SQLiteLexer(charStream);
	    
	    //se organizan los token producidos por el lexer para ser consumidos por el parse
	    CommonTokenStream tokens = new CommonTokenStream(lexer);
	    
	    //se crea un parse, realiza el analisis sintactico
	    SQLiteParser parser = new SQLiteParser(tokens); 
	    
	    //se utiliza el manejador de errores Bail, esta arroja una excepcion
	    //cuando se encuentran errores de sintacticos
	    parser.setErrorHandler(new BailErrorStrategy());
	    
	    //se devuelve el parse listo para ser analizado y recorrido
	    return parser;
	}
    
	
	/* Implementacion del metodo abtracto de la clase Refacoring donde
	 * se chequean las precondiciones que se deben cumplir para poder aplicar el refactoring */
    @Override
	protected boolean checkPreconditions(String text) {
		SQLiteParser parser;
		ParseTree newParseTree;
		
		//se crea el parse y se analiza que no tenga errores
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
        
		//herramienta que utilizamos para poder ver los arboles de las consultas que ingresabamos
		//se puede quitar los comentarios de las 2 loineas siguientes para poder ver los arboles
        //TreeViewer viewer = new TreeViewer(Arrays.asList(parser.getRuleNames()), newParseTree);
        //viewer.open();
		
        conEspacios.setLength(0);
        
        //se crea el visitor que se utilizara para recorrer el arbol
        ConditionVisitor visitor = new ConditionVisitor();
        
        //se visita e imprime
	    visitor.visit(newParseTree); 
        imprimirNodos(newParseTree);
        return visitor.getPrecondicion();    	     		
	}

    /* Implementacion del metodo abstracto de la clase Refactorin donde se realiza
     * la transformacion de una consulta*/
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
        return transformedText;
	}

	/* Implementacion del metodo abtracto de la clase Refacoring donde
	 * se chequean las post condiciones que se deben cumplir para poder
	 * considerar que el refactoring fue aplicado correctamente*/
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
        TreeViewer viewer = new TreeViewer(Arrays.asList(parser.getRuleNames()), newParseTree);
        viewer.open();
        ConditionVisitor postConditionsVisitor = new ConditionVisitor();
        postConditionsVisitor.visit(newParseTree);
        conEspacios.setLength(0);
        imprimirNodos(newParseTree);
        System.out.println(arreglarString());
        return postConditionsVisitor.getPostcondicion();
	}
	
	/* Metodo privado utilizado para agregar espacios al string que contiene una
	 * consulta SQLite, donde se recorre el arbol formado por la consulta
	 * y se van guardando los textos de los tokens en un stringBuilder*/
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
	
	/* Metodo privado para eliminar los espacios en blanco entre los puntos
	 * y los nombres de las columnas y le quita el <EOF> al final del string*/
	private String arreglarString() {
		return conEspacios.toString().replaceAll("\\s*\\.\\s*", ".").replaceAll(" ; <EOF>", ";");
	}
	
}