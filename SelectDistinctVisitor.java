import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import sqlitegrammar.*;

public class SelectDistinctVisitor extends SQLiteParserBaseVisitor<String>{
    // override visitselect_core
    @Override
    public String visitSelect_core(SQLiteParser.Select_coreContext ctx) {
        // get the text of the select core
        String selectCoreText = ctx.getText();
        // if the select core contains the word "DISTINCT"
        if (selectCoreText.contains("SELECT DISTINCT")) {
            // return the text of the select core
            System.out.println("retorno el ctx.getTex()");
            String result = ctx.getText(); 
            System.out.println(result);
            return ctx.getText();
           
        }
        System.out.println("retorno null");
        return null;
    }
}
