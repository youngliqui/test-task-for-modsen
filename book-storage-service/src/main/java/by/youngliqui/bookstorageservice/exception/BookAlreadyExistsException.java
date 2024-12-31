package by.youngliqui.bookstorageservice.exception;

import by.youngliqui.bookstorageservice.exception.abstr.ResourceAlreadyExistsException;

public class BookAlreadyExistsException extends ResourceAlreadyExistsException {
    public BookAlreadyExistsException(String message) {
        super(message);
    }
}
