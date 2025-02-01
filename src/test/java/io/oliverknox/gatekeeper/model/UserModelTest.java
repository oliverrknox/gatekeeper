package io.oliverknox.gatekeeper.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class UserModelTest {

    private static final Long USER_ID = 1L;
    private static final String USERNAME = "TestUser";
    private static final String PASSWORD_HASH = "TestHash";

    @Test
    public void testEmptyConstructor() {
        var userModel = new UserModel();

        assertNull(userModel.getId());
        assertNull(userModel.getUsername());
        assertNull(userModel.getPasswordHash());
    }

    @Test
    public void testConstructor() {
        var userModel = new UserModel(USERNAME, PASSWORD_HASH);

        assertEquals(USERNAME, userModel.getUsername());
        assertEquals(PASSWORD_HASH, userModel.getPasswordHash());
        assertNull(userModel.getId());
    }

    @Test
    public void testSetId() {
        var userModel = new UserModel(USERNAME, PASSWORD_HASH);

        userModel.setId(USER_ID);

        assertEquals(USER_ID, userModel.getId());
    }
}
