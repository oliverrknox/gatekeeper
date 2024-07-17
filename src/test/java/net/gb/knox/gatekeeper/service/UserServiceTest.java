package net.gb.knox.gatekeeper.service;

import jakarta.persistence.EntityNotFoundException;
import net.gb.knox.gatekeeper.dto.CreateUserRequestDTO;
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
    private static final UserModel USER_MODEL = new UserModel("TestUser", "TestHash");

    @BeforeAll
    public static void beforeAll() {
        USER_MODEL.setId(USER_ID);
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
}
