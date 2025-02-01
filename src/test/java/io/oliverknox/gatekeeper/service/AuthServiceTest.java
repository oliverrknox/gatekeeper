package io.oliverknox.gatekeeper.service;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import io.oliverknox.gatekeeper.config.AuthenticationFixture;
import io.oliverknox.gatekeeper.dto.LoginRequestDTO;
import io.oliverknox.gatekeeper.exception.UnauthorisedException;
import io.oliverknox.gatekeeper.utility.JWTUtility;
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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class AuthServiceTest {

    private static final LoginRequestDTO LOGIN_REQUEST_DTO = new LoginRequestDTO(
            "TestUser",
            "TestPassword"
    );
    private static final String TOKEN = "jwt-token";
    private static final Cookie REFRESH_COOKIE = new Cookie("refresh", "refresh-token");

    @Autowired
    private AuthService authService;
    @MockBean
    private AuthenticationManager authenticationManager;
    @MockBean
    private JWTUtility jwtUtility;

    @Test
    public void testLogin() throws UnauthorisedException {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(AuthenticationFixture.AUTHENTICATION);
        when(jwtUtility.createToken(anyString())).thenReturn(TOKEN);
        when(jwtUtility.createRefreshTokenCookie(anyString())).thenReturn(REFRESH_COOKIE);

        var loginPair = authService.login(LOGIN_REQUEST_DTO);

        var tokenResponseDTO = loginPair.getLeft();
        var refreshCookie = loginPair.getRight();

        assertEquals(TOKEN, tokenResponseDTO.token());
        assertEquals(REFRESH_COOKIE, refreshCookie);
    }

    @Test
    public void testLoginException() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid credentials."));

        var exception = assertThrows(UnauthorisedException.class, () -> authService.login(LOGIN_REQUEST_DTO));

        assertNotNull(exception);
        assertEquals("Username and/or password is invalid.", exception.getMessage());
    }

    @Test
    public void testRefresh() throws UnauthorisedException {
        var claimsMock = mock(Claims.class);
        when(jwtUtility.verifyToken(anyString())).thenReturn(true);
        when(jwtUtility.decodeToken(anyString())).thenReturn(claimsMock);
        when(claimsMock.getSubject()).thenReturn("TestUser");
        when(jwtUtility.createToken(anyString())).thenReturn(TOKEN);
        when(jwtUtility.createRefreshTokenCookie(anyString())).thenReturn(REFRESH_COOKIE);

        var refreshPair = authService.refresh(REFRESH_COOKIE.getValue());

        var tokenResponseDTO = refreshPair.getLeft();
        var refreshCookie = refreshPair.getRight();

        assertEquals(TOKEN, tokenResponseDTO.token());
        assertEquals(REFRESH_COOKIE, refreshCookie);
    }

    @Test
    public void testRefreshException() {
        when(jwtUtility.verifyToken(anyString())).thenReturn(false);

        var exception = assertThrows(UnauthorisedException.class, () -> authService.refresh(REFRESH_COOKIE.getValue()));

        assertNotNull(exception);
        assertEquals("Unable to authenticate your token.", exception.getMessage());
    }
}
