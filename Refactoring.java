public abstract class Refactoring {
    public String refactor(String text) throws RefactoringException{
        if (!this.checkPreconditions(text)) {
            throw new RefactoringException("Preconditions not met.");
        }
        String refactoredText = this.transform(text);
        // if (!this.checkPostconditions(refactoredText)) {
        //     throw new RefactoringException("Postconditions not met.");
        // }
        return refactoredText;
    }
    protected abstract boolean checkPreconditions(String text);
    protected abstract String transform(String text);
    protected abstract boolean checkPostconditions(String text);
}