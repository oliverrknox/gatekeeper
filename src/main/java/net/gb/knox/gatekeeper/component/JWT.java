package net.gb.knox.gatekeeper.component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JWT {

    private final String secretBase64;
    private final String issuer;
    private final String audience;
    private final Long expireAfterMs;

    public JWT(
            @Value("${JWT_SECRET_BASE64}") String secretBase64,
            @Value("${gatekeeper.jwt.issuer}") String issuer,
            @Value("${gatekeeper.jwt.audience}") String audience,
            @Value("${gatekeeper.jwt.expire-after-ms}") Long expireAfterMs) {
        this.secretBase64 = secretBase64;
        this.issuer = issuer;
        this.audience = audience;
        this.expireAfterMs = expireAfterMs;
    }

    public String createToken(String username) {
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

        if (secretBytes.length < 256) {
            throw new SecurityException("The JWT secret key is not long enough for the chosen signing algorithm.");
        }

        return Keys.hmacShaKeyFor(secretBytes);
    }
}
