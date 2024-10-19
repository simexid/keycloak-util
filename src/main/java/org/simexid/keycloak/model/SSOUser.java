
package org.simexid.keycloak.model;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * This class is used to represent the user in the Keycloak server.
 */
public class SSOUser {

    
    private Long createdTimestamp;
    private String email;
    private Boolean emailVerified;
    private Boolean enabled;
    private List<FederatedIdentity> federatedIdentities;
    private String firstName;
    private String id;
    private String lastName;
    private Long notBefore;
    private String username;
    private HashMap<String, List<String>> attributes;

    public Long getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(Long createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(Boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public List<FederatedIdentity> getFederatedIdentities() {
        return federatedIdentities;
    }

    public void setFederatedIdentities(List<FederatedIdentity> federatedIdentities) {
        this.federatedIdentities = federatedIdentities;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Long getNotBefore() {
        return notBefore;
    }

    public void setNotBefore(Long notBefore) {
        this.notBefore = notBefore;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public HashMap<String, List<String>> getAttributes() {
        return attributes;
    }

    public void setAttributes(HashMap<String, List<String>> attributes) {
        this.attributes = attributes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SSOUser ssoUser = (SSOUser) o;
        return Objects.equals(id, ssoUser.id) && Objects.equals(username, ssoUser.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username);
    }
}