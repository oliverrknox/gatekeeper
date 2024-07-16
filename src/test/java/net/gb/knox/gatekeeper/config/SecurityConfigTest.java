package net.gb.knox.gatekeeper.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import static org.mockito.Mockito.mock;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class SecurityConfigTest {

    @Autowired
    private SecurityConfig securityConfig;

    private static final HttpSecurity HTTP_SECURITY = mock(HttpSecurity.class);

    @Test
    public void testSecurityFilterChain() throws Exception {
        var securityFilterChain = securityConfig.securityFilterChain(HTTP_SECURITY);
        assertNotNull(securityFilterChain);
    }

    @Test
    public void testPasswordEncoder() {
        var passwordEncoder = securityConfig.passwordEncoder();
        assertNotNull(passwordEncoder);
    }
}
