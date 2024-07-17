package net.gb.knox.gatekeeper.controller;

import net.gb.knox.gatekeeper.dto.CreateUserRequestDTO;
import net.gb.knox.gatekeeper.dto.UserResponseDTO;
import net.gb.knox.gatekeeper.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserControllerTest {

    @Autowired
    private UserController userController;

    @MockBean
    private UserService userService;

    private static final CreateUserRequestDTO CREATE_USER_REQUEST_DTO = new CreateUserRequestDTO("TestUser", "TestPassword1");
    private static final UserResponseDTO CREATE_USER_RESPONSE_DTO = new UserResponseDTO(1L, "TestUser");

    @Test
    public void testCreateUser() throws URISyntaxException {
        when(userService.createUser(CREATE_USER_REQUEST_DTO)).thenReturn(CREATE_USER_RESPONSE_DTO);

        var responseEntity = userController.createUser(CREATE_USER_REQUEST_DTO);
        var location = "/users/" + CREATE_USER_RESPONSE_DTO.id();

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getHeaders().getLocation());
        assertEquals(location, responseEntity.getHeaders().getLocation().toString());
        assertEquals(CREATE_USER_RESPONSE_DTO, responseEntity.getBody());
    }
}
