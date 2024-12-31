package by.youngliqui.bookstorageservice.exception;

import by.youngliqui.bookstorageservice.exception.abstr.ResourceAlreadyExistsException;

public class GenreAlreadyExistsException extends ResourceAlreadyExistsException {
    public GenreAlreadyExistsException(String message) {
        super(message);
    }
}
