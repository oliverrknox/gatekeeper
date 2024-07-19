package net.gb.knox.gatekeeper.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

@SpringBootTest
public class SecurityConfigTest {

    @Autowired
    private SecurityConfig securityConfig;

    private static final HttpSecurity HTTP_SECURITY = mock(HttpSecurity.class);
    private static final AuthenticationConfiguration AUTHENTICATION_CONFIGURATION = mock(AuthenticationConfiguration.class);

    @Test
    public void testSecurityFilterChain() throws Exception {
        var securityFilterChain = securityConfig.securityFilterChain(HTTP_SECURITY);
        assertNotNull(securityFilterChain);
    }

    @Test
    public void testAuthenticationManager() throws Exception {
        var authenticationManager = securityConfig.authenticationManager(AUTHENTICATION_CONFIGURATION);
        assertNotNull(authenticationManager);
    }

    @Test
    public void testPasswordEncoder() {
        var passwordEncoder = securityConfig.passwordEncoder();
        assertNotNull(passwordEncoder);
    }
}
