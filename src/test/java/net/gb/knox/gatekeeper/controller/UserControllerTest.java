package net.gb.knox.gatekeeper.controller;

import net.gb.knox.gatekeeper.dto.CreateUserRequestDTO;
import net.gb.knox.gatekeeper.dto.CreateUserResponseDTO;
import net.gb.knox.gatekeeper.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserControllerTest {

    @Autowired
    private UserController userController;

    @MockBean
    private UserService userService;

    private static final CreateUserRequestDTO CREATE_USER_REQUEST_DTO = new CreateUserRequestDTO("TestUser", "TestPassword1");
    private static final CreateUserResponseDTO CREATE_USER_RESPONSE_DTO = new CreateUserResponseDTO(1L, "TestUser");

    @Test
    public void testCreateUser() {
        when(userService.createUser(CREATE_USER_REQUEST_DTO)).thenReturn(CREATE_USER_RESPONSE_DTO);

        var response = userController.createUser(CREATE_USER_REQUEST_DTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(CREATE_USER_RESPONSE_DTO, response.getBody());
    }
}
