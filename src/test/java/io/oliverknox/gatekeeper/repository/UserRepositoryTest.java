package io.oliverknox.gatekeeper.repository;

import io.oliverknox.gatekeeper.exception.UserNotFoundException;
import io.oliverknox.gatekeeper.model.UserModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private UserRepository userRepository;

    private static final UserModel USER_MODEL = new UserModel("TestUser", "TestHash");

    @Test
    public void testSave() {
        var savedUser = userRepository.save(USER_MODEL);

        assertNotNull(savedUser.getId());
        assertEquals(USER_MODEL.getUsername(), savedUser.getUsername());
        assertEquals(USER_MODEL.getPasswordHash(), savedUser.getPasswordHash());
    }

    @Test
    public void testSaveAsUpdate() {
        final var username = "UpdateUser";
        var newUser = userRepository.save(USER_MODEL);

        var updatedUser = new UserModel(newUser.getId(), username, newUser.getPasswordHash());
        var savedUser = userRepository.save(updatedUser);

        assertNotNull(savedUser.getId());
        assertEquals(username, savedUser.getUsername());
        assertEquals(USER_MODEL.getPasswordHash(), newUser.getPasswordHash());
    }

    @Test
    public void testFindById() {
        var savedUser = userRepository.save(USER_MODEL);
        entityManager.flush();

        var foundUser = userRepository.findById(savedUser.getId()).orElseThrow();

        assertEquals(savedUser.getId(), foundUser.getId());
        assertEquals(USER_MODEL.getUsername(), foundUser.getUsername());
        assertEquals(USER_MODEL.getPasswordHash(), foundUser.getPasswordHash());
    }

    @Test
    public void testFindByIdException() {
        var exception = assertThrows(UserNotFoundException.class, () -> {
            userRepository.findById(1L).orElseThrow(UserNotFoundException::new);
        });

        assertNotNull(exception);
    }

    @Test
    public void testFindByUsername() {
        var savedUser = userRepository.save(USER_MODEL);
        entityManager.flush();

        var foundUser = userRepository.findByUsername(USER_MODEL.getUsername());

        assertEquals(savedUser.getId(), foundUser.getId());
        assertEquals(USER_MODEL.getUsername(), foundUser.getUsername());
        assertEquals(USER_MODEL.getPasswordHash(), foundUser.getPasswordHash());
    }
}
