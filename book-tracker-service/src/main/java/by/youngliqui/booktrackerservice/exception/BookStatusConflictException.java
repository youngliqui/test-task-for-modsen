package by.youngliqui.booktrackerservice.exception;

public class BookStatusConflictException extends RuntimeException {
    public BookStatusConflictException(String message) {
        super(message);
    }
}
