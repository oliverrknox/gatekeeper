package net.gb.knox.gatekeeper.service;

import net.gb.knox.gatekeeper.component.JWT;
import net.gb.knox.gatekeeper.dto.LoginRequestDTO;
import net.gb.knox.gatekeeper.dto.TokenResponseDTO;
import net.gb.knox.gatekeeper.exception.UnauthorisedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JWT jwt;

    public TokenResponseDTO login(LoginRequestDTO loginRequestDTO) throws UnauthorisedException {
        try {
            var authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequestDTO.username(), loginRequestDTO.password())
            );

            if (authentication.isAuthenticated()) {
                var token = jwt.createToken(loginRequestDTO.username());
                return new TokenResponseDTO(token);
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
}
