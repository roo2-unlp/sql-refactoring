import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import sqlitegrammar.*;

public class AliasVisitor extends SQLiteParserBaseVisitor<String> {

    @Override
    public String visitColumn_alias(SQLiteParser.Column_aliasContext ctx){
    	
    	if (ctx.getText().equals("alias")){
	        //Todavia no se bien como funciona
	        return ctx.getText();
	        /* .column_alias().getText().equals(alias);
	        return contexto; */
    	}
    }
    
    public String visitTable_alias(SQLiteParser.Table_aliasContext ctx) {
    	if (ctx.getText().equals("alias")){
	        return ctx.getText();
    	}
    }
    
    public String visitAlias(SQLiteParser.AliasContext ctx) {
    	return ctx.getText();
    }
    
    /* 
     * // Que no exista otro alias igual al que quiero cambiar
		visitor.visitAliasNotExist(newParseTree);

		// No utilizar palabras reservadas CONSULTAR
		// se chequea al setear el nuevo alias

		// Que el nuevo alias no exista
		visitor.visitNewAliasNotExist(newParseTree);

		// Que el nuevo alias no sea igual al nombre de la tabla o de otra columna
		visitor.visitNewAliasNotEqualTable(newParseTree);
		visitor.visitNewAliasNotEqualColumn(newParseTree);

		// Que el cambio no genere conflictos en la subquery
     */
}
    
