package net.gb.knox.gatekeeper.controller;

import net.gb.knox.gatekeeper.dto.LoginRequestDTO;
import net.gb.knox.gatekeeper.dto.TokenResponseDTO;
import net.gb.knox.gatekeeper.exception.UnauthorisedException;
import net.gb.knox.gatekeeper.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
public class AuthControllerTest {

    private static final LoginRequestDTO LOGIN_REQUEST_DTO = new LoginRequestDTO(
            "TestUser",
            "Password1"
    );
    private static final TokenResponseDTO TOKEN_RESPONSE_DTO = new TokenResponseDTO("token");
    private static final UnauthorisedException UNAUTHORISED_EXCEPTION = new UnauthorisedException("Test unauthorised.");
    @Autowired
    private AuthController authController;
    @MockBean
    private AuthService authService;

    @Test
    public void testLogin() throws UnauthorisedException {
        when(authService.login(LOGIN_REQUEST_DTO)).thenReturn(TOKEN_RESPONSE_DTO);

        var responseEntity = authController.login(LOGIN_REQUEST_DTO);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(TOKEN_RESPONSE_DTO, responseEntity.getBody());
    }

    @Test
    public void testLoginException() throws UnauthorisedException {
        when(authService.login(LOGIN_REQUEST_DTO)).thenThrow(UNAUTHORISED_EXCEPTION);

        var exception = assertThrows(UnauthorisedException.class, () -> authController.login(LOGIN_REQUEST_DTO));

        assertEquals(UNAUTHORISED_EXCEPTION, exception);
    }
}
