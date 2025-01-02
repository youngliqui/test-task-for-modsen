package by.youngliqui.authservice.exception;

import by.youngliqui.authservice.exception.abstr.ResourceAlreadyExistsException;

public class UsernameAlreadyExistsException extends ResourceAlreadyExistsException {
    public UsernameAlreadyExistsException(String message) {
        super(message);
    }
}
