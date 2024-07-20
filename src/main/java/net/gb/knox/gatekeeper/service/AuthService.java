package net.gb.knox.gatekeeper.service;

import jakarta.servlet.http.Cookie;
import net.gb.knox.gatekeeper.dto.LoginRequestDTO;
import net.gb.knox.gatekeeper.dto.TokenResponseDTO;
import net.gb.knox.gatekeeper.exception.UnauthorisedException;
import net.gb.knox.gatekeeper.utility.JWTUtility;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JWTUtility jwtUtility;

    public ImmutablePair<TokenResponseDTO, Cookie> login(LoginRequestDTO loginRequestDTO) throws UnauthorisedException {
        try {
            var authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequestDTO.username(), loginRequestDTO.password())
            );

            if (authentication.isAuthenticated()) {
                var token = jwtUtility.createToken(loginRequestDTO.username());
                var refreshCookie = jwtUtility.createRefreshTokenCookie(loginRequestDTO.username());

                return ImmutablePair.of(new TokenResponseDTO(token), refreshCookie);
            }
        } catch (DisabledException ex) {
            throw new UnauthorisedException("This account is disabled.");
        } catch (LockedException ex) {
            throw new UnauthorisedException("This account is locked.");
        } catch (BadCredentialsException ex) {
            throw new UnauthorisedException("Username and/or password is invalid.");
        }

        throw new UnauthorisedException("Unable to authenticate your account.");
    }


    public ImmutablePair<TokenResponseDTO, Cookie> refresh(String refreshToken) throws UnauthorisedException {
        if (jwtUtility.verifyToken(refreshToken)) {
            var claims = jwtUtility.decodeToken(refreshToken);
            var username = claims.getSubject();

            var token = jwtUtility.createToken(username);
            var refreshCookie = jwtUtility.createRefreshTokenCookie(username);

            return ImmutablePair.of(new TokenResponseDTO(token), refreshCookie);
        }

        throw new UnauthorisedException("Unable to authenticate your token.");
    }
}
