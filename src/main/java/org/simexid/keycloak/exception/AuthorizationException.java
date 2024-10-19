package org.simexid.keycloak.exception;

public class AuthorizationException extends Exception {

    public AuthorizationException() {
        super("Authorization denied from keycloak");
    }

    public AuthorizationException(String message) {
        super(message);
    }

    public AuthorizationException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthorizationException(Throwable cause) {
        super(cause);
    }
}
