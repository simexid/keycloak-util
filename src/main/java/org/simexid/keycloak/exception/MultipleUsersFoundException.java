package org.simexid.keycloak.exception;

public class MultipleUsersFoundException extends Throwable {
    public MultipleUsersFoundException(String message) {
        super(message);
    }

    public MultipleUsersFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public MultipleUsersFoundException(Throwable cause) {
        super(cause);
    }

    public MultipleUsersFoundException() {
        super();
    }

}
