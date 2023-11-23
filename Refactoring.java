public abstract class Refactoring {
    public String refactor(String text) throws RefactoringException {
        System.out.println("----------- this.checkPreconditions(text) -----------------------");
        if (!this.checkPreconditions(text)) {
            System.out.println("Estoy en el else de checkPrecondition");
            throw new RefactoringException("Preconditions not met.");
        }
        System.out.println("Si muetsro este mensaje es que pase por if (!this.checkPreconditions(text))");
        String refactoredText = this.transform(text);
        if (!this.checkPostconditions(refactoredText)) {
            throw new RefactoringException("Postconditions not met.");
        }
        return refactoredText;
    }

    protected abstract boolean checkPreconditions(String text);

    protected abstract String transform(String text);

    protected abstract boolean checkPostconditions(String text);
}