package org.simexid.keycloak.exception;

public class MultipleRolesFoundException extends Throwable {
    public MultipleRolesFoundException(String message) {
        super(message);
    }

    public MultipleRolesFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public MultipleRolesFoundException(Throwable cause) {
        super(cause);
    }

    public MultipleRolesFoundException() {
        super();
    }
}
