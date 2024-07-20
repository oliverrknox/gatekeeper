package net.gb.knox.gatekeeper.utility;

import org.junit.jupiter.api.Test;
import org.opentest4j.TestAbortedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class JWTUtilityTest {

    @Autowired
    private JWTUtility jwtUtility;
    @Autowired
    private Environment environment;

    @Test
    public void testCreateToken() {
        var token = jwtUtility.createToken("TestUser");
        assertNotNull(token);
    }

    @Test
    public void testVerifyToken() {
        var token = jwtUtility.createToken("TestUser");
        var isVerified = jwtUtility.verifyToken(token);

        assertTrue(isVerified);
    }

    @Test
    public void testDecodeToken() {


        var token = jwtUtility.createToken("TestUser");
        System.out.println(token);
        var claims = jwtUtility.decodeToken(token);

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

    @Test
    public void testCreateRefreshTokenCookie() {
        var refreshExpireAfterMs = environment.getProperty("gatekeeper.jwt.refresh-expire-after-ms", Long.class);
        if (refreshExpireAfterMs == null) {
            throw new TestAbortedException("Property ${gatekeeper.jwt.expire-after-ms} was unable to be cast to a Long.");
        }

        var refreshCookie = jwtUtility.createRefreshTokenCookie("TestUser");

        assertEquals(JWTUtility.REFRESH_COOKIE_NAME, refreshCookie.getName());
        assertNotNull(refreshCookie.getValue());
        assertTrue(refreshCookie.isHttpOnly());
        assertTrue(refreshCookie.getSecure());
        assertEquals("/", refreshCookie.getPath());
        assertEquals(refreshExpireAfterMs / 1000, refreshCookie.getMaxAge());
    }
}
