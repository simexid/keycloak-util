package org.simexid.keycloak;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * Configuration properties for Simexid Keycloak integration.
 */
@Component
@PropertySource("classpath:simexid-keycloak.properties")
@ConfigurationProperties(prefix = "simexid.security.keycloak")
public class SimexidKeycloakConfigurationProperties {

    /**
     * The URL of the Keycloak server.
     */
    private String authServerUrl;

    /**
     * The realm of the Keycloak server.
     */
    private String realm;

    /**
     * The client ID of the Keycloak server.
     */
    private String clientId;

    /**
     * The client UUID of the Keycloak server.
     */
    private String clientUiid;

    /**
     * The client secret of the Keycloak server.
     */
    private String clientSecret;

    /**
     * The grant type requester of the Keycloak server by client.
     */
    private String grantType;

    /**
     * The URL of the Keycloak server admin (automatically generated from the previous parameter).
     */
    private String adminUrl;

    /**
     * The URL of the Keycloak server realm (automatically generated from the previous parameter).
     */
    private String realmUrl;

    /**
     * The URL of the Keycloak server token (automatically generated from the previous parameter).
     */
    private String tokenUrl;

    /**
     * The URL of the Keycloak server user (automatically generated from the previous parameter).
     */
    private String userUrl;

    public String getAuthServerUrl() {
        return authServerUrl;
    }

    public void setAuthServerUrl(String authServerUrl) {
        this.authServerUrl = authServerUrl;
    }

    public String getRealm() {
        return realm;
    }

    public void setRealm(String realm) {
        this.realm = realm;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientUiid() {
        return clientUiid;
    }

    public void setClientUiid(String clientUiid) {
        this.clientUiid = clientUiid;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getGrantType() {
        return grantType;
    }

    public void setGrantType(String grantType) {
        this.grantType = grantType;
    }

    public String getAdminUrl() {
        return adminUrl;
    }

    public void setAdminUrl(String adminUrl) {
        this.adminUrl = adminUrl;
    }

    public String getRealmUrl() {
        return realmUrl;
    }

    public void setRealmUrl(String realmUrl) {
        this.realmUrl = realmUrl;
    }

    public String getTokenUrl() {
        return tokenUrl;
    }

    public void setTokenUrl(String tokenUrl) {
        this.tokenUrl = tokenUrl;
    }

    public String getUserUrl() {
        return userUrl;
    }

    public void setUserUrl(String userUrl) {
        this.userUrl = userUrl;
    }
}
