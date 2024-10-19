
package org.simexid.keycloak.model;

import com.google.gson.annotations.Expose;

/**
 * This class is used to represent the federated identity of a user in the Keycloak server.
 */
public class FederatedIdentity {

    @Expose
    private String identityProvider;
    @Expose
    private String userId;
    @Expose
    private String userName;

    public String getIdentityProvider() {
        return identityProvider;
    }

    public void setIdentityProvider(String identityProvider) {
        this.identityProvider = identityProvider;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

}
