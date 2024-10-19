package org.simexid.keycloak.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

/**
 * This class is used to represent the token response from the Keycloak server.
 */
public class KeycloakTokenResponse {

    @JsonProperty("access_token")
    String accessToken;

    @JsonProperty("expires_in")
    long exp;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public long getExp() {
        return exp;
    }

    public void setExp(long exp) {
        this.exp = exp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        KeycloakTokenResponse that = (KeycloakTokenResponse) o;

        if (!Objects.equals(accessToken, that.accessToken)) return false;
        return Objects.equals(exp, that.exp);
    }

    @Override
    public int hashCode() {
        return accessToken != null ? accessToken.hashCode() : 0;
    }
}
