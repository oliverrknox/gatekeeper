package io.oliverknox.gatekeeper.controller;

import io.oliverknox.gatekeeper.dto.CreateUserRequestDTO;
import io.oliverknox.gatekeeper.dto.UpdateUserRequestDTO;
import io.oliverknox.gatekeeper.dto.UserResponseDTO;
import io.oliverknox.gatekeeper.exception.UserNotFoundException;
import io.oliverknox.gatekeeper.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserControllerTest {

    @Autowired
    private UserController userController;

    @MockBean
    private UserService userService;

    private static final Long USER_ID = 1L;
    private static final CreateUserRequestDTO CREATE_USER_REQUEST_DTO = new CreateUserRequestDTO(
            "TestUser",
            "TestPassword1"
    );
    private static final UpdateUserRequestDTO UPDATE_USER_REQUEST_DTO = new UpdateUserRequestDTO(
            "UpdateUser",
            "UpdatedPassword1"
    );
    private static final UserResponseDTO USER_RESPONSE_DTO = new UserResponseDTO(USER_ID, "TestUser");
    private static final UserResponseDTO UPDATED_USER_RESPONSE_DTO = new UserResponseDTO(
            USER_ID,
            UPDATE_USER_REQUEST_DTO.username()
    );
    private static final UserNotFoundException USER_NOT_FOUND_EXCEPTION = new UserNotFoundException(
            "Test user not found."
    );

    @Test
    public void testCreateUser() throws URISyntaxException {
        when(userService.createUser(CREATE_USER_REQUEST_DTO)).thenReturn(USER_RESPONSE_DTO);

        var responseEntity = userController.createUser(CREATE_USER_REQUEST_DTO);
        var location = "/users/" + USER_RESPONSE_DTO.id();

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getHeaders().getLocation());
        assertEquals(location, responseEntity.getHeaders().getLocation().toString());
        assertEquals(USER_RESPONSE_DTO, responseEntity.getBody());
    }

    @Test
    public void testGetUser() throws UserNotFoundException {
        when(userService.getUser(USER_ID)).thenReturn(USER_RESPONSE_DTO);

        var responseEntity = userController.getUser(USER_ID);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(USER_RESPONSE_DTO, responseEntity.getBody());
    }

    @Test
    public void testGetUserException() throws UserNotFoundException {
        when(userService.getUser(USER_ID)).thenThrow(USER_NOT_FOUND_EXCEPTION);

        var exception = assertThrows(UserNotFoundException.class, () -> userController.getUser(USER_ID));

        assertEquals(USER_NOT_FOUND_EXCEPTION, exception);
    }

    @Test
    public void testUpdateUser() throws UserNotFoundException {
        when(userService.updateUser(USER_ID, UPDATE_USER_REQUEST_DTO)).thenReturn(UPDATED_USER_RESPONSE_DTO);

        var responseEntity = userController.updateUser(USER_ID, UPDATE_USER_REQUEST_DTO);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(UPDATED_USER_RESPONSE_DTO, responseEntity.getBody());
    }

    @Test
    public void testUpdateUserException() throws UserNotFoundException {
        when(userService.updateUser(USER_ID, UPDATE_USER_REQUEST_DTO)).thenThrow(USER_NOT_FOUND_EXCEPTION);

        var exception = assertThrows(UserNotFoundException.class, () -> userController.updateUser(USER_ID, UPDATE_USER_REQUEST_DTO));

        assertEquals(USER_NOT_FOUND_EXCEPTION, exception);
    }

    @Test
    public void testDeleteUser() throws UserNotFoundException {
        var responseEntity = userController.deleteUser(USER_ID);

        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    }

    @Test
    public void testDeleteUserException() throws UserNotFoundException {
        doThrow(USER_NOT_FOUND_EXCEPTION).when(userService).deleteUser(USER_ID);

        var exception = assertThrows(UserNotFoundException.class, () -> userController.deleteUser(USER_ID));

        assertEquals(USER_NOT_FOUND_EXCEPTION, exception);
    }
}
