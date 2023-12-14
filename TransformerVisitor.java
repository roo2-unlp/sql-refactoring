import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.antlr.v4.runtime.CommonToken;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.TerminalNodeImpl;
import java.util.List;
import java.util.ArrayList;


public class TransformerVisitor extends SQLiteParserBaseVisitor<String> {
   
    @Override
    public String visitSelect_core(SQLiteParser.Select_coreContext ctx) {

        // Imprimir el objeto ctx
        //System.out.println("Contenido de ctx: " + ctx.getText());

        // Obtener la lista de instancias de Result_columnContext
        List<SQLiteParser.Result_columnContext> resultColumns = ctx.result_column();
        // Lista para almacenar todas las instancias de ExprContext
        List<SQLiteParser.ExprContext> exprList = new ArrayList<>();

        // Verificar si la palabra clave DISTINCT está presente
        TerminalNode distinctToken = ctx.DISTINCT_();
        
        if (distinctToken != null) {
            //System.out.println("La palabra clave DISTINCT está presente.");
            //Elimino la palabra clave DISTINCT
            ctx.children.remove(distinctToken.getSymbol().getTokenIndex() - 1);
        }

        // Iterar sobre la lista de Result_columnContext
        for (SQLiteParser.Result_columnContext resultColumn : resultColumns) {
            // Obtener la lista de instancias de ExprContext de cada Result_columnContext
            SQLiteParser.ExprContext expr = resultColumn.expr();  // Cambiado a ExprContext

            // Agregar la instancia a la lista
            exprList.add(expr);

        }

        if (!exprList.isEmpty()) {

            // Creo el token de GROUP
            Token groupToken = new CommonToken(SQLiteParser.GROUP_, "GROUP");
            TerminalNodeImpl groupNode = new TerminalNodeImpl(groupToken);

            // Creo el token de BY
            Token byToken = new CommonToken(SQLiteParser.BY_, "BY");
            TerminalNodeImpl byNode = new TerminalNodeImpl( byToken);

            //Agrego los nodos al arbol
            ctx.children.add(groupNode);
            ctx.children.add(byNode);

            // Declarar la variable firstExpr
            boolean firstExpr = true;


            for (SQLiteParser.ExprContext expr : exprList) {

                if (!firstExpr) {
                    // Si no es la primera expresión, agrega un token de coma antes de la nueva expresión
                    Token commaToken = new CommonToken(SQLiteParser.COMMA, ",");
                    TerminalNodeImpl commaNode = new TerminalNodeImpl(commaToken);
                    ctx.children.add(commaNode);
                } else {
                    firstExpr = false; // Cambia la bandera después de la primera expresión
                }
                //System.out.println("Expression in List: " + expr.getText());

                //Creo el token para la nueva expr
                Token newExprToken = new CommonToken(SQLiteParser.IDENTIFIER, expr.getText());
                TerminalNodeImpl newExprNode = new TerminalNodeImpl(newExprToken);

                // Agregar la nueva expresión al nuevo contexto
                ctx.children.add(newExprNode); 
                
                
            }

        }


        // Imprimir el objeto ctx
        //System.out.println("NUEVO Contenido de ctx: " +  ctx.getText());
        // Puedes devolver algún valor según tus necesidades
        return null;
    }
}