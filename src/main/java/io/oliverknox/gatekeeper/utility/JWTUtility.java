package io.oliverknox.gatekeeper.utility;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JWTUtility {

    public static final String REFRESH_COOKIE_NAME = "gatekeeper-refresh-token";

    private final String secretBase64;
    private final String issuer;
    private final String audience;
    private final Long expireAfterMs;
    private final Long refreshExpireAfterMs;

    public JWTUtility(
            @Value("${JWT_SECRET_BASE64}") String secretBase64,
            @Value("${gatekeeper.jwt.issuer}") String issuer,
            @Value("${gatekeeper.jwt.audience}") String audience,
            @Value("${gatekeeper.jwt.expire-after-ms}") Long expireAfterMs,
            @Value("${gatekeeper.jwt.refresh-expire-after-ms}") Long refreshExpireAfterMs) {
        this.secretBase64 = secretBase64;
        this.issuer = issuer;
        this.audience = audience;
        this.expireAfterMs = expireAfterMs;
        this.refreshExpireAfterMs = refreshExpireAfterMs;
    }

    public String createToken(String username, Long expireAfterMs) {
        return Jwts
                .builder()
                .subject(username)
                .issuer(issuer)
                .audience()
                .add(audience)
                .and()
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expireAfterMs))
                .signWith(getSecretKey())
                .compact();
    }

    public String createToken(String username) {
        return createToken(username, expireAfterMs);
    }

    public Cookie createRefreshTokenCookie(String username) {
        var token = createToken(username, refreshExpireAfterMs);

        var cookie = new Cookie(REFRESH_COOKIE_NAME, token);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge((int) (refreshExpireAfterMs / 1000));

        return cookie;
    }

    public Boolean verifyToken(String token) {
        return Jwts
                .parser()
                .verifyWith(getSecretKey())
                .build()
                .isSigned(token);
    }

    public Claims decodeToken(String token) {
        return Jwts
                .parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSecretKey() {
        byte[] secretBytes = Decoders.BASE64.decode(secretBase64);
        return Keys.hmacShaKeyFor(secretBytes);
    }
}
