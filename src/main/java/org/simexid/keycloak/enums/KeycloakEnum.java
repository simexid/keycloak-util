package org.simexid.keycloak.enums;

/**
 * Enum class for Keycloak
 */
public interface KeycloakEnum {

    /**
     * Enum representing the type of Keycloak entity.
     */
    public enum Type {
        REALM("realm"),
        CLIENT("client");

        private final String text;

        /**
         * Constructor for the enum.
         *
         * @param text the text representation of the enum value
         */
        Type(String text) {this.text = text;}

        /**
         * Gets the text representation of the enum value.
         *
         * @return the text representation
         */
        public String getText() {return this.text;}

        /**
         * Converts a string to the corresponding enum value.
         *
         * @param text the text to convert
         * @return the corresponding enum value, or null if no match is found
         */
        public static Type fromString(String text) {for (Type b : Type.values()) {
            if (b.text.equalsIgnoreCase(text)) {return b;}} return null;
        }
    }

    /**
     * Enum representing the type of user search in Keycloak.
     */
    public enum SearchUserType {
        USERNAME("username"),
        EMAIL("email"),
        FIRST_NAME("firstName"),
        LAST_NAME("lastName"),
        GENERIC_SEARCH("search");

        private final String text;

        /**
         * Constructor for the enum.
         *
         * @param text the text representation of the enum value
         */
        SearchUserType(String text) {this.text = text;}

        /**
         * Gets the text representation of the enum value.
         *
         * @return the text representation
         */
        public String getText() {return this.text;}

        /**
         * Converts a string to the corresponding enum value.
         *
         * @param text the text to convert
         * @return the corresponding enum value, or null if no match is found
         */
        public static SearchUserType fromString(String text) {for (SearchUserType b : SearchUserType.values()) {
            if (b.text.equalsIgnoreCase(text)) {return b;}} return null;
        }
    }
}
