package net.gb.knox.gatekeeper.repository;

import jakarta.persistence.EntityNotFoundException;
import net.gb.knox.gatekeeper.model.UserModel;
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
    public void testGetReferenceById() {
        var savedUser = userRepository.save(USER_MODEL);
        entityManager.flush();

        var foundUser = userRepository.getReferenceById(savedUser.getId());

        assertEquals(savedUser.getId(), foundUser.getId());
        assertEquals(USER_MODEL.getUsername(), foundUser.getUsername());
        assertEquals(USER_MODEL.getPasswordHash(), foundUser.getPasswordHash());
    }

    @Test
    public void testGetReferenceByIdException() {
        var exception = assertThrows(EntityNotFoundException.class, () -> {
            // `getReferenceById` is lazy, so it will only throw after accessing proxy.
            //  noinspection ResultOfMethodCallIgnored
            userRepository
                    .getReferenceById(1L)
                    .getUsername();
        });

        assertNotNull(exception);
    }
}
