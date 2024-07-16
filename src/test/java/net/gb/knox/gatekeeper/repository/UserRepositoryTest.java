package net.gb.knox.gatekeeper.repository;

import net.gb.knox.gatekeeper.model.UserModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private static final UserModel USER_MODEL = new UserModel("TestUser", "TestHash");

    @Test
    public void testSaveUser() {
        var savedUser = userRepository.save(USER_MODEL);

        assertNotNull(savedUser.getId());
        assertEquals("TestUser", savedUser.getUsername());
        assertEquals("TestHash", savedUser.getPasswordHash());
    }
}
