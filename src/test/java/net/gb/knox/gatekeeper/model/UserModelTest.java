package net.gb.knox.gatekeeper.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class UserModelTest {

    @Test
    public void testConstructor() {
        var userModel = new UserModel("TestUser", "abc-def-ghi");

        assertEquals("TestUser", userModel.getUsername());
        assertEquals("abc-def-ghi", userModel.getPasswordHash());
        assertNull(userModel.getId());
    }

    @Test
    public void testSetId() {
        var userModel = new UserModel("TestUser", "abc-def-ghi");

        userModel.setId(1L);

        assertEquals(1L, userModel.getId());
    }
}
