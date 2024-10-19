package org.simexid.keycloak.exception;

public class UserNotFoundException extends Exception {

    public UserNotFoundException() {
        super("User not found in keycloak database");
    }

    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserNotFoundException(Throwable cause) {
        super(cause);
    }
}
