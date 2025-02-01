package io.oliverknox.gatekeeper.service;

import jakarta.servlet.http.Cookie;
import io.oliverknox.gatekeeper.dto.LoginRequestDTO;
import io.oliverknox.gatekeeper.dto.TokenResponseDTO;
import io.oliverknox.gatekeeper.exception.UnauthorisedException;
import io.oliverknox.gatekeeper.utility.JWTUtility;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JWTUtility jwtUtility;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public ImmutablePair<TokenResponseDTO, Cookie> login(LoginRequestDTO loginRequestDTO) throws UnauthorisedException {
        logger.info("ENTER login | username={}", loginRequestDTO.username());
        try {
            var authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequestDTO.username(), loginRequestDTO.password())
            );

            if (authentication.isAuthenticated()) {
                var token = jwtUtility.createToken(loginRequestDTO.username());
                var refreshCookie = jwtUtility.createRefreshTokenCookie(loginRequestDTO.username());

                logger.info("EXIT login | username={}", loginRequestDTO.username());
                return ImmutablePair.of(new TokenResponseDTO(token), refreshCookie);
            }
        } catch (DisabledException ex) {
            logger.warn("DisabledException login | message={}", ex.getMessage());
            throw new UnauthorisedException("This account is disabled.");
        } catch (LockedException ex) {
            logger.warn("LockedException login | message={}", ex.getMessage());
            throw new UnauthorisedException("This account is locked.");
        } catch (BadCredentialsException ex) {
            logger.warn("BadCredentialsException login | message={}", ex.getMessage());
            throw new UnauthorisedException("Username and/or password is invalid.");
        }

        logger.warn("UnauthorisedException login | message=Unable to authenticate your account.");
        throw new UnauthorisedException("Unable to authenticate your account.");
    }

    public ImmutablePair<TokenResponseDTO, Cookie> refresh(String refreshToken) throws UnauthorisedException {
        logger.info("ENTER refresh");
        if (jwtUtility.verifyToken(refreshToken)) {
            var claims = jwtUtility.decodeToken(refreshToken);
            var username = claims.getSubject();
            logger.info("IN refresh | username={}", username);

            var token = jwtUtility.createToken(username);
            var refreshCookie = jwtUtility.createRefreshTokenCookie(username);

            logger.info("EXIT refresh | username={}", username);
            return ImmutablePair.of(new TokenResponseDTO(token), refreshCookie);
        }

        logger.warn("UnauthorisedException login | message=Unable to authenticate your token.");
        throw new UnauthorisedException("Unable to authenticate your token.");
    }
}
