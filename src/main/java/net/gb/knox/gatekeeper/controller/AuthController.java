package net.gb.knox.gatekeeper.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import net.gb.knox.gatekeeper.annotation.ForbiddenApiResponse;
import net.gb.knox.gatekeeper.annotation.GenericErrorApiResponse;
import net.gb.knox.gatekeeper.annotation.UnauthorisedApiResponse;
import net.gb.knox.gatekeeper.dto.LoginRequestDTO;
import net.gb.knox.gatekeeper.dto.TokenResponseDTO;
import net.gb.knox.gatekeeper.exception.UnauthorisedException;
import net.gb.knox.gatekeeper.service.AuthService;
import net.gb.knox.gatekeeper.utility.JWTUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Operation(summary = "Login as an existing user.")
    @ApiResponse(
            responseCode = "200",
            description = "Logged in.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = TokenResponseDTO.class)
            )
    )
    @UnauthorisedApiResponse
    @GenericErrorApiResponse
    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO,
                                                  HttpServletResponse httpServletResponse) throws UnauthorisedException {
        logger.info("ENTER login | username={}", loginRequestDTO.username());

        var loginPair = authService.login(loginRequestDTO);

        var tokenResponseDTO = loginPair.getLeft();
        var refreshCookie = loginPair.getRight();

        httpServletResponse.addCookie(refreshCookie);

        logger.info("EXIT login | username={}", loginRequestDTO.username());
        return ResponseEntity.ok(tokenResponseDTO);
    }

    @Operation(
            summary = "Refresh an existing access token.",
            security = @SecurityRequirement(name = "jwt")
    )
    @ApiResponse(
            responseCode = "200",
            description = "Refreshed token.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = TokenResponseDTO.class)
            )
    )
    @UnauthorisedApiResponse
    @ForbiddenApiResponse
    @GenericErrorApiResponse
    @PostMapping("/refresh")
    public ResponseEntity<TokenResponseDTO> refresh(@CookieValue(value = JWTUtility.REFRESH_COOKIE_NAME) String refreshToken,
                                                    HttpServletResponse httpServletResponse) throws UnauthorisedException {
        logger.info("ENTER refresh");
        var refreshPair = authService.refresh(refreshToken);

        var tokenResponseDTO = refreshPair.getLeft();
        var refreshCookie = refreshPair.getRight();

        httpServletResponse.addCookie(refreshCookie);

        logger.info("EXIT refresh");
        return ResponseEntity.ok(tokenResponseDTO);
    }
}
