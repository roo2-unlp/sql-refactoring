import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import sqlitegrammar.*;

public class LikeVisitor extends SQLiteParserBaseVisitor<String> {
    @Override
    public String visitParse(SQLiteParser.ParseContext ctx) {
        String value = ctx.getText(); // tomo el texto
        String refactoredValue = value.replace("%", ""); // le saco todos los %
        return refactoredValue.concat("%"); // le agrego un unico % al final y se lo devuelvo
    }
}
