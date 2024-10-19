package org.simexid.keycloak.exception;

public class AttributesException extends Exception {

    public AttributesException() {
        super("User not found in keycloak database");
    }

    public AttributesException(String message) {
        super(message);
    }

    public AttributesException(String message, Throwable cause) {
        super(message, cause);
    }

    public AttributesException(Throwable cause) {
        super(cause);
    }
}
