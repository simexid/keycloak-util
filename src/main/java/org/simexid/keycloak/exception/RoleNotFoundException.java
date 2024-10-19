package org.simexid.keycloak.exception;

public class RoleNotFoundException extends Exception {

    public RoleNotFoundException() {
        super("Role not found in keycloak database");
    }

    public RoleNotFoundException(String message) {
        super(message);
    }

    public RoleNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public RoleNotFoundException(Throwable cause) {
        super(cause);
    }
}
