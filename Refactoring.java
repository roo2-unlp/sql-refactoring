import org.antlr.v4.runtime.tree.ParseTree;

public abstract class Refactoring {
    public ParseTree refactor(ParseTree tree) throws RefactoringException{
        if (!this.checkPreconditions(tree)) {
            throw new RefactoringException("Preconditions not met.");
        }
        ParseTree refactoredTree = this.transform(tree);
        if (!this.checkPostconditions(refactoredTree)) {
            throw new RefactoringException("Postconditions not met.");
        }
        return refactoredTree;
    }
    protected abstract boolean checkPreconditions(ParseTree tree);
    protected abstract ParseTree transform(ParseTree tree);
    protected abstract boolean checkPostconditions(ParseTree tree);
}