package by.youngliqui.booktrackerservice.exception;

import by.youngliqui.booktrackerservice.exception.abstr.ResourceConflictException;

public class BookStatusConflictException extends ResourceConflictException {
    public BookStatusConflictException(String message) {
        super(message);
    }
}
