import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import sqlitegrammar.*;

import org.antlr.v4.runtime.Token;

public class LimitWithOrderByVisitor extends SQLiteParserBaseVisitor<String> {

    private StringBuilder transformedText = new StringBuilder();
    private int limit = 10;
    
    public void setLimit(int limit){
        this.limit = limit;
    }

    @Override
    public String visitTerminal(TerminalNode node) {
        // Verificar si el nodo es EOF
        if (node.getSymbol().getType() == Token.EOF) {
            // No agregues el EOF a la consulta, solo devuelve el texto transformado
            int length = transformedText.length();
            if (length > 0 && transformedText.charAt(length - 1) == ' ') {
                transformedText.deleteCharAt(length - 1);
            }
            // agrego el limit a la consulta cuando llego al final
            return transformedText.append(" LIMIT ").append(limit).append(";").toString();
            // return transformedText.append(";").toString();
        }

        // Obtener el texto del nodo
        String nodeText = node.getText();

        // Verificar si el nodo es una coma, un punto o un punto y coma
        if (".".contains(nodeText)){
            int length = transformedText.length();
            if (length > 0 && transformedText.charAt(length - 1) == ' ') {
                transformedText.deleteCharAt(length - 1);
            }
            transformedText.append(node.getText());
        }
        else{
           if (",".contains(nodeText)) {
                // Eliminar el espacio anterior si existe
                int length = transformedText.length();
                if (length > 0 && transformedText.charAt(length - 1) == ' ') {
                    transformedText.deleteCharAt(length - 1);
                }
            }
            // Agregar el texto del nodo al constructor de la consulta
            transformedText.append(node.getText()).append(" ");
        }
        return null;
    }

}