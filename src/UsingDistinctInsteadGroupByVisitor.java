import org.antlr.v4.runtime.CommonToken;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.antlr.v4.runtime.tree.TerminalNodeImpl;
import sqlitegrammar.SQLiteParser;
import sqlitegrammar.SQLiteParserBaseVisitor;

public class UsingDistinctInsteadGroupByVisitor extends SQLiteParserBaseVisitor<String> {
	
    public UsingDistinctInsteadGroupByVisitor() {

    }
    
    @Override
    public String visitSelect_core(SQLiteParser.Select_coreContext ctx) {
        int selectIndice = -1;
        int groupByIndice = -1;

        for (int i = 0; i < ctx.children.size(); i++) {
            ParseTree hijo = ctx.children.get(i);

            if (hijo instanceof TerminalNode) {
                TerminalNode terminalNode = (TerminalNode) hijo;
                int tipoSimbolo = terminalNode.getSymbol().getType();

                if (tipoSimbolo == SQLiteParser.SELECT_) {
                    selectIndice = i;

                    Token distinctToken = new CommonToken(SQLiteParser.DISTINCT_, "DISTINCT");
                    TerminalNodeImpl distinctNode = new TerminalNodeImpl(distinctToken);

                    ctx.children.add(selectIndice + 1, distinctNode);
                } else if (tipoSimbolo == SQLiteParser.GROUP_) {
                    groupByIndice = i;
                    break;
                }
            }
        }

        if (groupByIndice != -1) {
            while (ctx.children.size() > groupByIndice) {
                ctx.children.remove(groupByIndice);
            }
        }

        return super.visitSelect_core(ctx);
    }
}
