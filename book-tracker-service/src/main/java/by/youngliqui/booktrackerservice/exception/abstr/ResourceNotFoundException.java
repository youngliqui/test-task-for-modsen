package by.youngliqui.booktrackerservice.exception.abstr;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
