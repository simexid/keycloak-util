package org.simexid.keycloak.service;

import org.simexid.keycloak.enums.KeycloakEnum;
import org.simexid.keycloak.exception.*;
import org.simexid.keycloak.model.KeycloakTokenResponse;
import org.simexid.keycloak.model.SSORoles;
import org.simexid.keycloak.model.SSOUser;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Objects;

import com.google.gson.Gson;

/**
 * Utility class for interacting with Keycloak.
 */
@Service
public class KeycloakUtil {

    public KeycloakUtil() {
    }

    @Value("${simexid.security.keycloak.token-url}")
    private String tokenUrl;

    @Value("${simexid.security.keycloak.user-url}")
    private String userUrl;

    @Value("${simexid.security.keycloak.admin-url}")
    private String adminUrl;

    @Value("${simexid.security.keycloak.client-id}")
    private String clientId;

    @Value("${simexid.security.keycloak.client-uuid}")
    private String clientUiid;

    @Value("${simexid.security.keycloak.client-secret}")
    private String clientSecret;

    @Value("${simexid.security.keycloak.grant-type}")
    private String grantType;

    private String token = "";
    private long expiration;

    private HttpHeaders headers = new HttpHeaders();
    private MultiValueMap<String, String> map= new LinkedMultiValueMap<>();

    /**
     * Handles input for Keycloak authentication.
     */
    private void handleInputForKeycloakAuth() {
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        if (map.isEmpty()) {
            map.add("client_id", clientId);
            map.add("client_secret", clientSecret);
            map.add("grant_type", grantType);
        }
    }

    /**
     * Authorizes the client with Keycloak. The token is stored in memory until it expires.
     *
     * @return true if authorization is successful, false otherwise
     * @throws AuthorizationException if an error occurs during authorization
     */
    public boolean authorized() throws AuthorizationException {
        long now = new Date().getTime();
        if (!token.isEmpty() && now<expiration) {
            return true;
        }
        try {
            RestTemplate rest = new RestTemplate();
            handleInputForKeycloakAuth();
            HttpEntity<MultiValueMap<String, String>> request =
                    new HttpEntity<>(map, headers);
            ResponseEntity<KeycloakTokenResponse> response = rest.postForEntity(tokenUrl, request, KeycloakTokenResponse.class);
            KeycloakTokenResponse body = response.getBody();

            if (response.getStatusCode().is2xxSuccessful() && body != null
                    && !body.getAccessToken().isBlank()
                    && body.getExp() > 0) {
                token = body.getAccessToken();
                expiration = new Date().getTime() + (body.getExp() * 1000);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            throw new AuthorizationException();
        }
    }

    /**
     * Retrieves the user information from Keycloak.
     *
     * @param sub the user ID
     * @return the user information
     * @throws GenericException if an error occurs during the operation
     * @throws AuthorizationException if the client is not authorized
     */
    public SSOUser getUserInfo(String sub) throws GenericException, AuthorizationException {
        if (!authorized()) {
            return null;
        }
        try {
            RestTemplate rest = new RestTemplate();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(token);
            HttpEntity<String> request =
                    new HttpEntity<>(null, headers);

            ResponseEntity<String> response = rest.exchange(userUrl + "/" + sub, HttpMethod.GET, request, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                return new Gson().fromJson(response.getBody(), SSOUser.class);
            } else if (response.getStatusCode().isSameCodeAs(HttpStatus.valueOf(404))) {
                return null;
            } else {
                throw new GenericException();
            }
        } catch (Exception e) {
            throw new GenericException();
        }
    }

    /**
     * Updates a user in Keycloak.
     *
     * @param sub the user ID
     * @param user the user sso representation
     * @return true if the user was updated successfully, false otherwise
     * @throws AuthorizationException if the client is not authorized
     * @throws GenericException if an error occurs during the operation
     */
    public boolean updateUser(String sub, SSOUser user) throws AuthorizationException, GenericException {
        if (!authorized()) {
            return false;
        }
        try {
            RestTemplate rest = new RestTemplate();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(token);
            HttpEntity<SSOUser> request =
                    new HttpEntity<>(user, headers);

            ResponseEntity<String> response = rest.exchange(userUrl + "/" + sub, HttpMethod.PUT, request, String.class);

            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            throw new GenericException();
        }
    }

    /**
     * Adds attributes to a user in Keycloak.
     *
     * @param sub the user ID
     * @param attributes the attributes to add
     * @return true if the attributes were added successfully, false otherwise
     * @throws AuthorizationException if the client is not authorized
     * @throws GenericException if an error occurs during the operation
     * @throws AttributesException if an error occurs during assigment of attributes
     */
    public boolean addUserAttributes(String sub, List<HashMap<String, List<String>>> attributes) throws AuthorizationException, GenericException, AttributesException {
        SSOUser user = getUserInfo(sub);
        for (HashMap<String, List<String>> attribute : attributes) {
            for (Map.Entry<String, List<String>> entry : attribute.entrySet()) {
                if (user.getAttributes()==null) {
                    user.setAttributes(new HashMap<>());
                }
                user.getAttributes().put(entry.getKey(), entry.getValue());
            }
        }
        String payload = "{" +
                "\"email\":\""+user.getEmail()+"\","+
                "\"attributes\":" +new Gson().toJson(user.getAttributes())+ "}" +
                "}";
        return callForAddAttributes(sub, payload);
    }

    /**
     * Deletes attributes from a user in Keycloak.
     *
     * @param sub the user ID
     * @param attributes the attributes to delete
     * @return true if the attributes were deleted successfully, false otherwise
     * @throws AuthorizationException if the client is not authorized
     * @throws GenericException if an error occurs during the operation
     * @throws AttributesException if an error occurs during assigment of attributes
     */
    public boolean deleteUserAttributes(String sub, List<String> attributes) throws AuthorizationException, GenericException, AttributesException {
        SSOUser user = getUserInfo(sub);
        for (String attribute : attributes) {
            user.getAttributes().remove(attribute);
        }
        String payload = "{" +
                "\"email\":\""+user.getEmail()+"\","+
                "\"attributes\":" +new Gson().toJson(user.getAttributes())+ "}" +
                "}";
        return callForAddAttributes(sub, payload);
    }

    /**
     * Calls Keycloak to add/remove (rewrite) attributes to a user.
     *
     * @param sub the user ID
     * @param payload the payload containing the attributes
     * @return true if the attributes were added successfully, false otherwise
     * @throws AuthorizationException if the client is not authorized
     * @throws AttributesException if an error occurs during assigment of attributes
     */
    public boolean callForAddAttributes(String sub, String payload) throws AuthorizationException, AttributesException {
        if (!authorized()) {
            return false;
        }
        try {
            RestTemplate rest = new RestTemplate();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(token);
            HttpEntity<String> request =
                    new HttpEntity<>(payload, headers);

            ResponseEntity<String> response = rest.exchange(userUrl + "/" + sub, HttpMethod.PUT, request, String.class);

            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            throw new AttributesException();
        }
    }

    /**
     * Adds a role to a user in Keycloak.
     *
     * @param searchUserType the type of search
     * @param searchText the search text
     * @param role the role to add
     * @param type the type of role (REALM or CLIENT)
     * @param clientUUID the client UUID
     * @return true if the role was added successfully, false otherwise
     * @throws MultipleUsersFoundException if multiple users are found
     * @throws MultipleRolesFoundException if multiple roles are found
     * @throws AuthorizationException if the client is not authorized
     * @throws GenericException if an error occurs during the operation
     */
    public boolean addRoleToUser(KeycloakEnum.SearchUserType searchUserType, String searchText, String role, KeycloakEnum.Type type, String clientUUID) throws MultipleUsersFoundException, MultipleRolesFoundException, AuthorizationException, GenericException {
        if (clientUUID==null || clientUUID.isBlank()) {
            clientUUID = clientUiid;
        }
        List<SSOUser> users = searchUser(searchUserType, searchText);
        if (users.size() > 1) {
            throw new MultipleUsersFoundException();
        }
        List<SSORoles> roles = searchRoles(role, type, clientUUID);
        if (roles.size() > 1) {
            throw new MultipleRolesFoundException();
        }
        List<SSORoles> userRole = retrieveUserRole(users.get(0).getId(), type, clientUUID);
        userRole.add(roles.get(0));
        String payload = new Gson().toJson(userRole);
        return callForAddRole(users.get(0).getId(), payload, type, clientUUID);
    }

    /**
     * Deletes a role from a user in Keycloak.
     *
     * @param searchUserType the type of search
     * @param searchText the search text
     * @param role the role to delete
     * @param type the type of role (REALM or CLIENT)
     * @param clientUUID the client UUID
     * @return true if the role was deleted successfully, false otherwise
     * @throws MultipleUsersFoundException if multiple users are found
     * @throws MultipleRolesFoundException if multiple roles are found
     * @throws AuthorizationException if the client is not authorized
     * @throws GenericException if an error occurs during the operation
     */
    public boolean deleteRoleToUser(KeycloakEnum.SearchUserType searchUserType, String searchText, String role, KeycloakEnum.Type type, String clientUUID) throws MultipleUsersFoundException, MultipleRolesFoundException, AuthorizationException, GenericException {
        if (clientUUID==null || clientUUID.isBlank()) {
            clientUUID = clientUiid;
        }
        List<SSOUser> users = searchUser(searchUserType, searchText);
        if (users.size() > 1) {
            throw new MultipleUsersFoundException();
        }
        List<SSORoles> roles = searchRoles(role, type, clientUUID);
        if (roles.size() > 1) {
            throw new MultipleRolesFoundException();
        }
        List<SSORoles> userRole = retrieveUserRole(users.get(0).getId(), type, clientUUID);
        userRole.remove(roles.get(0));
        String payload = new Gson().toJson(userRole);
        return callForAddRole(users.get(0).getId(), payload, type, clientUUID);
    }

    /**
     * Calls Keycloak to add/remove (rewrite) a role to a user.
     *
     * @param sub the user ID
     * @param payload the payload containing the role
     * @param type the type of role (REALM or CLIENT)
     * @param clientUUID the client UUID
     * @return true if the role was added successfully, false otherwise
     * @throws AuthorizationException if the client is not authorized
     * @throws GenericException if an error occurs during the operation
     */
    public boolean callForAddRole(String sub, String payload, KeycloakEnum.Type type, String clientUUID) throws AuthorizationException, GenericException {
        if (!authorized()) {
            return false;
        }
        try {
            String path;
            switch (type) {
                case REALM:
                    path = "/role-mappings/realm";
                    break;
                case CLIENT:
                    path = "/role-mappings/clients/"+clientUUID;
                    break;
                default:
                    return false;
            }
            RestTemplate rest = new RestTemplate();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(token);
            HttpEntity<String> request =
                    new HttpEntity<>(payload, headers);

            ResponseEntity<String> response = rest.exchange(userUrl+"/"+sub+path, HttpMethod.POST, request, String.class);

            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            throw new GenericException();
        }
    }

    /**
     * Searches for a user in Keycloak.
     *
     * @param searchUserType the type of search
     * @param searchText the search text
     * @return the user information
     * @throws AuthorizationException if the client is not authorized
     * @throws GenericException if an error occurs during the operation
     */
    public List<SSOUser> searchUser(KeycloakEnum.SearchUserType searchUserType, String searchText) throws AuthorizationException, GenericException {
        if (!authorized()) {
            return null;
        }
        try {
            RestTemplate rest = new RestTemplate();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(token);
            HttpEntity<String> request =
                    new HttpEntity<>(null, headers);

            ResponseEntity<List<SSOUser>> response = rest.exchange(userUrl + "?"+searchUserType.getText()+"=" + searchText , HttpMethod.GET, request, new ParameterizedTypeReference<List<SSOUser>>() {});

            if (response.getStatusCode().is2xxSuccessful()) {
                if (Objects.requireNonNull(response.getBody()).isEmpty()) {
                    throw new UserNotFoundException();
                }
                return Objects.requireNonNull(response.getBody());
            } else {
                throw new GenericException();
            }
        } catch (Exception e) {
            throw new GenericException(e);
        }
    }

    /**
     * Searches for roles in Keycloak.
     *
     * @param name the role name
     * @param type the type of role (REALM or CLIENT)
     * @param clientUUID the client UUID
     * @return the role information
     * @throws AuthorizationException if the client is not authorized
     * @throws GenericException if an error occurs during the operation
     */
    public List<SSORoles> searchRoles(String name, KeycloakEnum.Type type, String clientUUID) throws AuthorizationException, GenericException {
        if (!authorized()) {
            return null;
        }
        try {
            String path;
            switch (type) {
                case REALM:
                    path = "/roles";
                    break;
                case CLIENT:
                    path = "/clients/"+clientUUID+"/roles";
                    break;
                default:
                    return null;
            }
            RestTemplate rest = new RestTemplate();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(token);
            HttpEntity<String> request =
                    new HttpEntity<>(null, headers);

            ResponseEntity<List<SSORoles>> response = rest.exchange(adminUrl + path + "?search=" + name , HttpMethod.GET, request, new ParameterizedTypeReference<List<SSORoles>>() {});

            if (response.getStatusCode().is2xxSuccessful()) {
                return Objects.requireNonNull(response.getBody());
            } else {
                throw new GenericException();
            }
        } catch (Exception e) {
            throw new GenericException(e);
        }
    }

    /**
     * Retrieves the roles of a user in Keycloak.
     *
     * @param sub the user ID
     * @param type the type of role (REALM or CLIENT)
     * @param clientUIID the client UUID
     * @return the list of roles
     * @throws AuthorizationException if the client is not authorized
     * @throws GenericException if an error occurs during the operation
     */
    public List<SSORoles> retrieveUserRole(String sub, KeycloakEnum.Type type, String clientUIID) throws AuthorizationException, GenericException {
        if (!authorized()) {
            return null;
        }
        try {
            String urlAppend;
            switch (type) {
                case REALM:
                    urlAppend = "/role-mappings/realm";
                    break;
                case CLIENT:
                    urlAppend = "/role-mappings/clients/"+clientUIID;
                    break;
                default:
                    return null;
            }
            RestTemplate rest = new RestTemplate();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(token);
            HttpEntity<String> request =
                    new HttpEntity<>(null, headers);

            ResponseEntity<List<SSORoles>> response = rest.exchange(userUrl + "/" + sub + urlAppend, HttpMethod.GET, request, new ParameterizedTypeReference<List<SSORoles>>() {});

        if (response.getStatusCode().is2xxSuccessful()) {
            return Objects.requireNonNull(response.getBody());
        } else {
            throw new GenericException();
        }
        } catch (Exception e) {
            throw new GenericException(e);
        }
    }

    /**
     * Get the current keycloak token and expiration
     *
     * @return an HashMap with the token string as "token" and the expiration in long format as "expiration" if authorized, else a null object
     * @throws AuthorizationException if the client is not authorized
     */
    public HashMap<String, Object> getToken() throws AuthorizationException {
        if (authorized() && !token.isEmpty() && expiration>0) {
            HashMap<String, Object> tokenObj = new HashMap<>();
            tokenObj.put("token", token);
            tokenObj.put("expiration", expiration);
            return tokenObj;
        } else {
            return null;
        }
    }
}
