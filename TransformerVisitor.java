import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.antlr.v4.runtime.CommonToken;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.TerminalNodeImpl;
//import sqlitegrammar.SQLiteParser;
//import sqlitegrammar.SQLiteParserBaseVisitor;

public class TransformerVisitor extends SQLiteParserBaseVisitor<String> {
   
    @Override
    public String visitSelect_core(SQLiteParser.Select_coreContext ctx) {
        int distintIndice = -1;
        ParseTree node = null;
        /**int groupByIndice = -1;*/

        for (int i = 0; i < ctx.children.size(); i++) {
            ParseTree hijo = ctx.children.get(i);

            if (hijo instanceof TerminalNode) {
                TerminalNode terminalNode = (TerminalNode) hijo;
                int tipoSimbolo = terminalNode.getSymbol().getType();

                if (tipoSimbolo == SQLiteParser.DISTINCT_) {
                    distintIndice = i;
                    
                    /**
                    Token distinctToken = new CommonToken(SQLiteParser.DISTINCT_, "DISTINCT");
                    TerminalNodeImpl distinctNode = new TerminalNodeImpl(distinctToken);

                    ctx.children.add(selectIndice + 1, distinctNode);
                    */
                    int result_columnIndice = distintIndice + 1;
                    node = ctx.children.get(result_columnIndice);


                    ctx.children.remove(distintIndice);

                }
            }
        }

        if (distintIndice != -1) {

            Token groupToken = new CommonToken(SQLiteParser.GROUP_, "GROUP");
            TerminalNodeImpl grouptNode = new TerminalNodeImpl( groupToken);
            ctx.children.add(ctx.children.size(), grouptNode);

            Token byToken = new CommonToken(SQLiteParser.BY_, "BY");
            TerminalNodeImpl byNode = new TerminalNodeImpl(byToken);
            ctx.children.add(ctx.children.size(), byNode);

            ctx.children.add(ctx.children.size(), node);
        }

        return super.visitSelect_core(ctx);
    }
}