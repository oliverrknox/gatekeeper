package net.gb.knox.gatekeeper.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class OpenAPIConfigTest {

    @Autowired
    private OpenAPIConfig openAPIConfig;

    @Test
    public void testOpenAPI() {
        var openAPI = openAPIConfig.openAPI();
        assertNotNull(openAPI);
    }
}
