package by.youngliqui.bookstorageservice.exception;

import by.youngliqui.bookstorageservice.exception.abstr.ResourceNotFoundException;

public class GenreNotFoundException extends ResourceNotFoundException {
    public GenreNotFoundException(String message) {
        super(message);
    }
}
