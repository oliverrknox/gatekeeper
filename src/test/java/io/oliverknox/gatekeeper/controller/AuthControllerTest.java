package io.oliverknox.gatekeeper.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import io.oliverknox.gatekeeper.dto.LoginRequestDTO;
import io.oliverknox.gatekeeper.dto.TokenResponseDTO;
import io.oliverknox.gatekeeper.exception.UnauthorisedException;
import io.oliverknox.gatekeeper.service.AuthService;
import io.oliverknox.gatekeeper.utility.JWTUtility;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class AuthControllerTest {

    private static final LoginRequestDTO LOGIN_REQUEST_DTO = new LoginRequestDTO(
            "TestUser",
            "Password1"
    );
    private static final TokenResponseDTO TOKEN_RESPONSE_DTO = new TokenResponseDTO("token");
    private static final UnauthorisedException UNAUTHORISED_EXCEPTION = new UnauthorisedException("Test unauthorised.");
    private static final HttpServletResponse HTTP_SERVLET_RESPONSE_MOCK = mock(HttpServletResponse.class);

    private Cookie refreshCookie;

    @Autowired
    private AuthController authController;
    @Autowired
    private JWTUtility jwtUtility;
    @MockBean
    private AuthService authService;

    @BeforeEach
    public void setup() {
        reset(HTTP_SERVLET_RESPONSE_MOCK);

        refreshCookie = new Cookie("refresh", jwtUtility.createToken("TestUser"));
    }

    @Test
    public void testLogin() throws UnauthorisedException {
        when(authService.login(LOGIN_REQUEST_DTO)).thenReturn(ImmutablePair.of(TOKEN_RESPONSE_DTO, refreshCookie));

        var responseEntity = authController.login(LOGIN_REQUEST_DTO, HTTP_SERVLET_RESPONSE_MOCK);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(TOKEN_RESPONSE_DTO, responseEntity.getBody());
        verify(HTTP_SERVLET_RESPONSE_MOCK, times(1))
                .addCookie(
                        argThat(cookie -> cookie.getName().equals(refreshCookie.getName()))
                );
    }

    @Test
    public void testLoginException() throws UnauthorisedException {
        when(authService.login(LOGIN_REQUEST_DTO)).thenThrow(UNAUTHORISED_EXCEPTION);

        var exception = assertThrows(UnauthorisedException.class, () -> authController.login(LOGIN_REQUEST_DTO, HTTP_SERVLET_RESPONSE_MOCK));

        assertEquals(UNAUTHORISED_EXCEPTION, exception);
    }

    @Test
    public void testRefresh() throws UnauthorisedException {
        when(authService.refresh(refreshCookie.getValue())).thenReturn(ImmutablePair.of(TOKEN_RESPONSE_DTO, refreshCookie));

        var responseEntity = authController.refresh(refreshCookie.getValue(), HTTP_SERVLET_RESPONSE_MOCK);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(TOKEN_RESPONSE_DTO, responseEntity.getBody());
        verify(HTTP_SERVLET_RESPONSE_MOCK, times(1))
                .addCookie(
                        argThat(cookie -> cookie.getName().equals(refreshCookie.getName()))
                );
    }

    @Test
    public void testRefreshException() throws UnauthorisedException {
        when(authService.refresh(refreshCookie.getValue())).thenThrow(UNAUTHORISED_EXCEPTION);

        var exception = assertThrows(UnauthorisedException.class, () -> authController.refresh(
                refreshCookie.getValue(),
                HTTP_SERVLET_RESPONSE_MOCK
        ));

        assertEquals(UNAUTHORISED_EXCEPTION, exception);
    }
}
