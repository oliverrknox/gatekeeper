package net.gb.knox.gatekeeper.service;

import jakarta.persistence.EntityNotFoundException;
import net.gb.knox.gatekeeper.dto.CreateUserRequestDTO;
import net.gb.knox.gatekeeper.dto.UpdateUserRequestDTO;
import net.gb.knox.gatekeeper.exception.UserNotFoundException;
import net.gb.knox.gatekeeper.model.UserModel;
import net.gb.knox.gatekeeper.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

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
    private static final CreateUserRequestDTO CREATE_USER_REQUEST_DTO = new CreateUserRequestDTO("TestUser", "TestPassword1");
    private static final UpdateUserRequestDTO UPDATE_USER_REQUEST_DTO = new UpdateUserRequestDTO("UpdatedUser");
    private static final UserModel USER_MODEL = new UserModel("TestUser", "TestHash");
    private static final UserModel UPDATED_USER_MODEL = new UserModel(UPDATE_USER_REQUEST_DTO.username(), USER_MODEL.getPasswordHash());

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
    public void testGetUserById() throws UserNotFoundException {
        when(userRepository.getReferenceById(anyLong())).thenReturn(USER_MODEL);

        var userResponseDTO = userService.getUserById(USER_ID);

        assertEquals(USER_ID, userResponseDTO.id());
        assertEquals(CREATE_USER_REQUEST_DTO.username(), userResponseDTO.username());
    }

    @Test
    public void testGetUserByIdException() {
        when(userRepository.getReferenceById(anyLong())).thenThrow(new EntityNotFoundException());

        var exception = assertThrows(UserNotFoundException.class, () -> userService.getUserById(USER_ID));

        assertNotNull(exception);
    }

    @Test
    public void testUpdateUser() throws UserNotFoundException {
        when(userRepository.getReferenceById(anyLong())).thenReturn(USER_MODEL);
        when(userRepository.save(any(UserModel.class))).thenReturn(UPDATED_USER_MODEL);

        var userResponseDTO = userService.updateUser(USER_ID, UPDATE_USER_REQUEST_DTO);

        assertEquals(USER_ID, userResponseDTO.id());
        assertEquals(UPDATE_USER_REQUEST_DTO.username(), userResponseDTO.username());
    }

    @Test
    public void testUpdateUserException() {
        when(userRepository.getReferenceById(anyLong())).thenThrow(new EntityNotFoundException());
        when(userRepository.save(any(UserModel.class))).thenReturn(UPDATED_USER_MODEL);

        var exception = assertThrows(
                UserNotFoundException.class,
                () -> userService.updateUser(USER_ID, UPDATE_USER_REQUEST_DTO)
        );

        assertNotNull(exception);
    }
}
