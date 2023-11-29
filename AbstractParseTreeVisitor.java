public class AbstractParseTreeVisitor<T> implements ParseTreeVisitor<T> {

    @Override
    protected String defaultResult() {
        return "";
    }

    @Override
    protected String aggregateResult(String aggregate, String nextResult) {
        return nextResult + aggregate;
    }
}
