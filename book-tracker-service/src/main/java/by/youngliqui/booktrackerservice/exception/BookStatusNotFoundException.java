package by.youngliqui.booktrackerservice.exception;

import by.youngliqui.booktrackerservice.exception.abstr.ResourceNotFoundException;

public class BookStatusNotFoundException extends ResourceNotFoundException {
    public BookStatusNotFoundException(String message) {
        super(message);
    }
}
