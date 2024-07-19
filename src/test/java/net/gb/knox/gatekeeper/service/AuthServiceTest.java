package net.gb.knox.gatekeeper.service;

import net.gb.knox.gatekeeper.component.JWT;
import net.gb.knox.gatekeeper.config.AuthenticationFixture;
import net.gb.knox.gatekeeper.dto.LoginRequestDTO;
import net.gb.knox.gatekeeper.exception.UnauthorisedException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
public class AuthServiceTest {

    private static final LoginRequestDTO LOGIN_REQUEST_DTO = new LoginRequestDTO(
            "TestUser",
            "TestPassword"
    );
    private static final String TOKEN = "jwt token";
    @Autowired
    private AuthService authService;
    @MockBean
    private AuthenticationManager authenticationManager;
    @MockBean
    private JWT jwt;

    @Test
    public void testLogin() throws UnauthorisedException {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(AuthenticationFixture.AUTHENTICATION);
        when(jwt.createToken(anyString())).thenReturn(TOKEN);

        var tokenResponseDTO = authService.login(LOGIN_REQUEST_DTO);

        assertEquals(TOKEN, tokenResponseDTO.token());
    }

    @Test
    public void testLoginException() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid credentials."));

        var exception = assertThrows(UnauthorisedException.class, () -> authService.login(LOGIN_REQUEST_DTO));

        assertNotNull(exception);
        assertEquals("Username and/or password is invalid.", exception.getMessage());
    }
}
