# Keycloak Util

**Keycloak Util** is a **Spring Boot** library that simplifies the configuration and usage of Keycloak admin APIs. This library is designed to easily integrate Keycloak into Spring Boot applications, providing an intuitive interface to manage authentication, roles, users, and other administrative features through the Keycloak admin APIs.

## Features
- Simplified Keycloak configuration for Spring Boot applications
- Pre-configured calls to Keycloak admin APIs
- Support for common operations like user and role management
- Seamless integration with the Spring Boot application context

## Requirements
- **Java 17** or higher
- **Spring Boot 3.0** or higher
- **Keycloak** 18.0 or higher

## Installation

Add the Maven dependency to your project:

```xml
<dependency>
    <groupId>org.simexid.keycloak</groupId>
    <artifactId>keycloak-util</artifactId>
    <version>1.0.0</version>
</dependency>
```
For Gradle users, add this dependency to your build.gradle file:
```groovy
implementation 'org.simexid.keycloak:keycloak-util:1.0.0'
```

## Configuration
To configure the library, add the following properties to your application.yml file:
    
```yaml
simexid:
  security:
    keycloak:
      auth-server-url: {auth-server-url}
      realm: {realm}
      client-id: {your-client-id}
      client-uuid: {your-client-uuid}
      client-secret: {your-client-secret}
```
These properties are mandatory and used to fill automatically other properties (admin-url, user-url, realm-url and toker-url)
If wanted, you can override these properties by adding them to your application.yml file:
```yaml
simexid:
  security:
    keycloak:
      admin-url: {admin-url}
      user-url: {user-url}
      realm-url: {realm-url}
      token-url: {token-url}
```
By default, the grant-type is set to client_credentials, but you can override it by adding the following property to your application.yml file:
```yaml
simexid:
  security:
    keycloak:
      grant-type: {grant-type}
```
## Usage
To use the Keycloak Util library, you can autowire the KeycloakService class in your Spring Boot application:

```java
import org.simexid.keycloak.service.KeycloakUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class YourService {
    @Autowired
    private KeycloakUtil keycloakUtil;
}
```
or (preferred way)

```java
import org.simexid.keycloak.service.KeycloakUtil;
import org.springframework.stereotype.Service;

@Service
public class YourService {
    private final KeycloakUtil keycloakUtil;

    public YourService(KeycloakUtil keycloakUtil) {
        this.keycloakUtil = keycloakUtil;
    }
}
```

The KeycloakUtil class provides methods to interact with the Keycloak admin APIs.
At this time, the following methods are available:

| **Method**             | **Input**                                                                        | **Description**                                                                                          |
|------------------------|----------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------|
| `authorized`           | None                                                                             | Checks if the client is authorized.                                                                      |
| `getUserInfo`          | `(String userId)`                                                                | Retrieves information about a user by their ID.                                                          |
| `searchUser`           | `(KeycloakUtil.SearchType searchType, String searchValue)`                       | Searches for users by email, username, ID, or text.                                                      |
| `addUserAttributes`    | `(String userId, List<HashMap<String, List>> attributes)`                        | Adds attributes to a user.                                                                               |
| `deleteUserAttributes` | `(String userId, List attributes)`                                               | Deletes specified attributes from a user.                                                                |
| `addRoleToUser`        | `(String userId, String roleName, KeycloakEnum.Type type, String clientUUID)`    | Adds a role to a user.                                                                                   |
| `deleteRoleToUser`     | `(String userId, String roleName, KeycloakEnum.Type type, String clientUUID)`    | Deletes a role from a user.                                                                              |
| `searchRoles`          | `(String name, KeycloakEnum.Type type, String clientUUID)`                       | Searches for roles by name.                                                                              |
| `retrieveUserRole`     | `(String sub, KeycloakEnum.Type type, String clientUUID)`                        | Retrieves the roles of a user.                                                                           |
| `callForAddRole`       | `(String userId, String jsonPayload, KeycloakEnum.Type type, String clientUUID)` | Calls the Keycloak API to override the role of a user. This overrides all the roles of the user.         |
| `callForAddAttributes` | `(String userId, String jsonPayload)`                                            | Calls the Keycloak API to override attributes for a user. This overrides all the attributes of the user. |
| `updateUser`           | `(String sub, SSOUser user)`                                                     | Update SSO user.                                                                                         |


The **Javadoc** is available [here](https://simexid.github.io/keycloak-util/apidocs/org/simexid/keycloak/service/KeycloakUtil.html).

Fot addRoleToUser, deleteRoleToUser, searchRoles and callForAddRole, the type parameter is used to specify if the role is a realm role or a client role. If the role is a client role, you can specified another clientUUID instead of the one specified in the application.yml. For realm role or default client-uuid leave null.

```java
import org.simexid.keycloak.service.KeycloakUtil;
import org.simexid.keycloak.enums.KeycloakEnum;
import org.simexid.keycloak.models.SSOUser;
import org.simexid.keycloak.models.SSORoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class KeycloakServiceExample {

    private final KeycloakUtil keycloakUtil;

    @Autowired
    public KeycloakServiceExample(KeycloakUtil keycloakUtil) {
        this.keycloakUtil = keycloakUtil;
    }

    public void exampleUsage() throws Exception {
        // Check if authorized
        boolean isAuthorized = keycloakUtil.authorized();
        System.out.println("Authorized: " + isAuthorized);

        // Get user info
        SSOUser user = keycloakUtil.getUserInfo("user-id"); //sub UUID
        System.out.println("User Info: " + user);
        
        // Search users
        List<SSOUser> users = keycloakUtil.searchUser(KeycloakUtil.SearchType.EMAIL, "email");
        System.out.println("Users: " + users);

        // Add user attributes
        List<HashMap<String, List<String>>> attributes = new ArrayList<>();
        HashMap<String, List<String>> attribute = new HashMap<>();
        attribute.put("attribute-name", List.of("attribute-value"));
        boolean addAttributesResult = keycloakUtil.addUserAttributes("user-id", attributes);
        System.out.println("Add Attributes Result: " + addAttributesResult);

        // Delete user attributes
        List<String> attributesToDelete = new ArrayList<>();
        attributesToDelete.add("attribute-name");
        boolean deleteAttributesResult = keycloakUtil.deleteUserAttributes("user-id", attributesToDelete);
        System.out.println("Delete Attributes Result: " + deleteAttributesResult);

        // Add role to user
        boolean addRoleResult = keycloakUtil.addRoleToUser("user-id", "role-name", KeycloakEnum.Type.REALM, null);
        System.out.println("Add Role Result: " + addRoleResult);

        // Delete role from user
        boolean deleteRoleResult = keycloakUtil.deleteRoleToUser("user-id", "role-name", KeycloakEnum.Type.REALM, null);
        System.out.println("Delete Role Result: " + deleteRoleResult);
        
        // Search roles
        List<SSORoles> roles = keycloakUtil.searchRoles("role-name", KeycloakEnum.Type.REALM, null);
        System.out.println("Roles: " + roles);

        // Retrieve user roles
        List<SSORoles> userRoles = keycloakUtil.retrieveUserRole("user-id", KeycloakEnum.Type.REALM, null);
        System.out.println("User Roles: " + userRoles);

        // Retrieve user roles for another client
        List<SSORoles> userRoles = keycloakUtil.retrieveUserRole("user-id", KeycloakEnum.Type.CLIENT, "client-uuid");
        System.out.println("User Roles: " + userRoles);
        
        // Call directly callForAddRole (this override all the role of the user)
        String jsonPayload = "{" +
                "\"email\":\"user@email\","+
                "\"attributes\":[\"myAttribute\": [\"value\"] ] \"}" +
                "}";
        boolean callForAddRole = keycloakUtil.callForAddRole("user-id", jsonPayload, KeycloakEnum.Type.REALM, null);
        System.out.println("Call For Add Role: " + callForAddRole);
        
        // Call directly callForAddAttributes (this override all the attributes of the user)
        String jsonPayload = "{" +
                "\"email\":\"user@email\","+
                "\"roles\": [\"myRole\"] \"}" +
                "}";
        boolean callForAddAttributes = keycloakUtil.callForAddAttributes("user-id", jsonPayload);
        System.out.println("Call For Add Attributes: " + callForAddAttributes);
        
        // Update user
        SSOUser user = keycloakUtil.getUserInfo("user-id");
        user.setFirstName("New First Name");
        user.setLastName("New Last Name");
        boolean updateUser = keycloakUtil.updateUser("user-id", user);
        System.out.println("Update User: " + updateUser);
        
    }
}
```

## Author
- **Simexid** - [www.simexid.org](https://www.simexid.org) - [Pietro Saccone]
- **Email** - [info@simexid.org](mailto:info@simexid.org)

## Contributions
Contributions are welcome! If you have any suggestions or feature requests, please create an issue or a pull request.

## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
```
MIT License

Copyright (c) 2024 Pietro Saccone (Simexid)

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
