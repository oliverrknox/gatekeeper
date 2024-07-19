package net.gb.knox.gatekeeper.component;

import org.junit.jupiter.api.Test;
import org.opentest4j.TestAbortedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class JWTTest {

    @Autowired
    private JWT jwt;
    @Autowired
    private Environment environment;

    @Test
    public void testCreateToken() {
        var token = jwt.createToken("TestUser");
        assertNotNull(token);
    }

    @Test
    public void testVerifyToken() {
        var token = jwt.createToken("TestUser");
        var isVerified = jwt.verifyToken(token);

        assertTrue(isVerified);
    }

    @Test
    public void testDecodeToken() {
        var expireAfterMs = environment.getProperty("gatekeeper.jwt.expire-after-ms", Long.class);
        if (expireAfterMs == null) {
            throw new TestAbortedException("Property ${gatekeeper.jwt.expire-after-ms} was unable to be cast to a Long.");
        }

        var token = jwt.createToken("TestUser");
        System.out.println(token);
        var claims = jwt.decodeToken(token);

        assertEquals("TestUser", claims.getSubject());
        assertEquals(environment.getProperty("gatekeeper.jwt.issuer"), claims.getIssuer());
        assertEquals(
                environment.getProperty("gatekeeper.jwt.audience"),
                claims.getAudience().stream().findFirst().orElse(null)
        );
        var now = new Date().getTime();
        assertTrue(
                claims.getIssuedAt().getTime() < now,
                "Expected " + claims.getIssuedAt().getTime() + " to be before " + now
        );
        assertTrue(claims.getExpiration().after(new Date()));
    }
}
