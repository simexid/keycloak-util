package org.simexid.keycloak;

import org.simexid.keycloak.service.KeycloakUtil;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * Auto-configuration class for Simexid Keycloak integration.
 */
@AutoConfiguration
@EnableConfigurationProperties(SimexidKeycloakConfigurationProperties.class)
public class SimexidKeycloakAutoConfiguration {

    /**
     * Creates a `KeycloakUtil` bean.
     *
     * @return a new instance of `KeycloakUtil`
     */
    @Bean
    public KeycloakUtil keycloakUtil() {
        return new KeycloakUtil();
    }

}