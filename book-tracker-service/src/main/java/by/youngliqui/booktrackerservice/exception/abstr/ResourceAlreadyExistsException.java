package by.youngliqui.booktrackerservice.exception.abstr;

public class ResourceAlreadyExistsException extends ResourceConflictException {
    public ResourceAlreadyExistsException(String message) {
        super(message);
    }
}
