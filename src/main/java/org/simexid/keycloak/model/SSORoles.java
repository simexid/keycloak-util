package org.simexid.keycloak.model;

import com.google.gson.Gson;

/**
 * This class is used to represent the roles of a user in the Keycloak server.
 */
public class SSORoles {
    String id;
    String name;
    String description;
    Boolean composite;
    Boolean clientRole;
    String containerId;

    public SSORoles() {
    }

    public SSORoles(String id, String name, String description, Boolean composite, Boolean clientRole, String containerId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.composite = composite;
        this.clientRole = clientRole;
        this.containerId = containerId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getComposite() {
        return composite;
    }

    public void setComposite(Boolean composite) {
        this.composite = composite;
    }

    public Boolean getClientRole() {
        return clientRole;
    }

    public void setClientRole(Boolean clientRole) {
        this.clientRole = clientRole;
    }

    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
