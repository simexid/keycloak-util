package org.simexid.keycloak.exception;

public class GenericException extends Exception {

    public GenericException() {
        super("Keycloak Generic Exception");
    }

    public GenericException(String message) {
        super(message);
    }

    public GenericException(String message, Throwable cause) {
        super(message, cause);
    }

    public GenericException(Throwable cause) {
        super(cause);
    }
}
