package am.itspace.projectscope.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException() {
    }

    public UserNotFoundException(int id) {
        super("User not found, id:" + id);
    }
}