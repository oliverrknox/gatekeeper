package io.oliverknox.gatekeeper.service;

import io.oliverknox.gatekeeper.dto.CreateUserRequestDTO;
import io.oliverknox.gatekeeper.dto.UpdateUserRequestDTO;
import io.oliverknox.gatekeeper.exception.UserNotFoundException;
import io.oliverknox.gatekeeper.model.UserModel;
import io.oliverknox.gatekeeper.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;
    @MockBean
    private PasswordEncoder passwordEncoder;

    private static final Long USER_ID = 1L;
    private static final CreateUserRequestDTO CREATE_USER_REQUEST_DTO = new CreateUserRequestDTO(
            "TestUser",
            "TestPassword1"
    );
    private static final UpdateUserRequestDTO UPDATE_USER_REQUEST_DTO = new UpdateUserRequestDTO(
            "UpdatedUser",
            "UpdatedPassword1"
    );
    private static final UserModel USER_MODEL = new UserModel("TestUser", "TestHash");
    private static final UserModel UPDATED_USER_MODEL = new UserModel(
            UPDATE_USER_REQUEST_DTO.username(),
            UPDATE_USER_REQUEST_DTO.password()
    );

    @BeforeAll
    public static void beforeAll() {
        USER_MODEL.setId(USER_ID);
        UPDATED_USER_MODEL.setId(USER_MODEL.getId());
    }

    @Test
    public void testCreateUser() {
        when(passwordEncoder.encode(CREATE_USER_REQUEST_DTO.password())).thenReturn("TestHash");
        when(userRepository.save(any(UserModel.class))).thenReturn(USER_MODEL);

        var userResponseDTO = userService.createUser(CREATE_USER_REQUEST_DTO);

        assertEquals(USER_ID, userResponseDTO.id());
        assertEquals(CREATE_USER_REQUEST_DTO.username(), userResponseDTO.username());
    }

    @Test
    public void testGetUser() throws UserNotFoundException {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(USER_MODEL));

        var userResponseDTO = userService.getUser(USER_ID);

        assertEquals(USER_ID, userResponseDTO.id());
        assertEquals(CREATE_USER_REQUEST_DTO.username(), userResponseDTO.username());
    }

    @Test
    public void testGetUserException() {
        var exception = assertThrows(UserNotFoundException.class, () -> userService.getUser(USER_ID));

        assertNotNull(exception);
    }

    @Test
    public void testUpdateUser() throws UserNotFoundException {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(USER_MODEL));
        when(userRepository.save(any(UserModel.class))).thenReturn(UPDATED_USER_MODEL);

        var userResponseDTO = userService.updateUser(USER_ID, UPDATE_USER_REQUEST_DTO);

        assertEquals(USER_ID, userResponseDTO.id());
        assertEquals(UPDATE_USER_REQUEST_DTO.username(), userResponseDTO.username());
    }

    @Test
    public void testUpdateUserException() {
        when(userRepository.save(any(UserModel.class))).thenReturn(UPDATED_USER_MODEL);

        var exception = assertThrows(
                UserNotFoundException.class,
                () -> userService.updateUser(USER_ID, UPDATE_USER_REQUEST_DTO)
        );

        assertNotNull(exception);
    }

    @Test
    public void testDeleteUser() throws UserNotFoundException {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(USER_MODEL));

        userService.deleteUser(USER_ID);
    }

    @Test
    public void testDeleteUserException() {
        var exception = assertThrows(UserNotFoundException.class, () -> userService.deleteUser(USER_ID));

        assertNotNull(exception);
    }
}
