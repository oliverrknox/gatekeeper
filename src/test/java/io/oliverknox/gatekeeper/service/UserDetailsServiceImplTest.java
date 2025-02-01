package io.oliverknox.gatekeeper.service;

import io.oliverknox.gatekeeper.model.UserModel;
import io.oliverknox.gatekeeper.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserDetailsServiceImplTest {

    private static final UserModel USER_MODEL = new UserModel("TestUser", "TestHash");
    @Autowired
    private UserDetailsService userDetailsService;
    @MockBean
    private UserRepository userRepository;

    @Test
    public void testLoadUserByUsername() {
        when(userRepository.findByUsername(anyString())).thenReturn(USER_MODEL);

        var user = userDetailsService.loadUserByUsername(USER_MODEL.getUsername());

        assertEquals(USER_MODEL.getUsername(), user.getUsername());
        assertEquals(USER_MODEL.getPasswordHash(), user.getPassword());
    }

    @Test
    public void testLoadUserByUsernameException() {
        when(userRepository.findByUsername(anyString())).thenThrow(new UsernameNotFoundException("No user found."));

        var exception = assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername(USER_MODEL.getUsername());
        });

        assertNotNull(exception);
    }
}
