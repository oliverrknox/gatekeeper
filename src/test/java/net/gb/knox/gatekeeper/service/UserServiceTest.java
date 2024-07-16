package net.gb.knox.gatekeeper.service;

import net.gb.knox.gatekeeper.dto.CreateUserRequestDTO;
import net.gb.knox.gatekeeper.model.UserModel;
import net.gb.knox.gatekeeper.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;
    @MockBean
    private PasswordEncoder passwordEncoder;

    private static final CreateUserRequestDTO CREATE_USER_REQUEST_DTO = new CreateUserRequestDTO("TestUser", "TestPassword1");
    private static final UserModel USER_MODEL = new UserModel("TestUser", "TestHash");

    @BeforeAll
    public static void beforeAll() {
        USER_MODEL.setId(1L);
    }

    @Test
    public void testCreateUser() {
        when(passwordEncoder.encode(CREATE_USER_REQUEST_DTO.password())).thenReturn("TestHash");
        when(userRepository.save(any(UserModel.class))).thenReturn(USER_MODEL);

        var createUserResponseDTO = userService.createUser(CREATE_USER_REQUEST_DTO);

        assertEquals(1L, createUserResponseDTO.id());
        assertEquals("TestUser", createUserResponseDTO.username());
    }
}
