
package org.simexid.keycloak.model;

import com.google.gson.Gson;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FederatedIdentity that = (FederatedIdentity) o;
        return identityProvider.equals(that.identityProvider) && userId.equals(that.userId) && userName.equals(that.userName);
    }

    @Override
    public int hashCode() {
        int result = identityProvider.hashCode();
        result = 31 * result + userId.hashCode();
        result = 31 * result + userName.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "FederatedIdentity{" +
                "identityProvider='" + identityProvider + '\'' +
                ", userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                '}';
    }

    public String toJson() {
        return new Gson().toJson(this);
    }
}
