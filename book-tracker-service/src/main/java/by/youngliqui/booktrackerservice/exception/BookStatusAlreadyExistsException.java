package by.youngliqui.booktrackerservice.exception;

import by.youngliqui.booktrackerservice.exception.abstr.ResourceAlreadyExistsException;

public class BookStatusAlreadyExistsException extends ResourceAlreadyExistsException {
    public BookStatusAlreadyExistsException(String message) {
        super(message);
    }
}
