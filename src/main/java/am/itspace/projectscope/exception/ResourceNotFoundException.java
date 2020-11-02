package am.itspace.projectscope.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException() {

    }

    public ResourceNotFoundException(int id) {
        super("Resource not found, id:" + id);
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
