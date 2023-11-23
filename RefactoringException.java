public class RefactoringException extends Exception{
    public RefactoringException(String message) {
        super(message);
        System.out.println("RefactoringException: " + message);
    }
}
