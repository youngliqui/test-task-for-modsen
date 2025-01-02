package by.youngliqui.authservice.exception;

import by.youngliqui.authservice.exception.abstr.ResourceNotFoundException;

public class UserNotFoundException extends ResourceNotFoundException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
