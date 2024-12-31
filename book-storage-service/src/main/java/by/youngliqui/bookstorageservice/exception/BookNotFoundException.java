package by.youngliqui.bookstorageservice.exception;

import by.youngliqui.bookstorageservice.exception.abstr.ResourceNotFoundException;

public class BookNotFoundException extends ResourceNotFoundException {
    public BookNotFoundException(String message) {
        super(message);
    }
}
