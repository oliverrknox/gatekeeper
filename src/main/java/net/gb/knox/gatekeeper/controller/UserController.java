package net.gb.knox.gatekeeper.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import net.gb.knox.gatekeeper.annotation.ForbiddenApiResponse;
import net.gb.knox.gatekeeper.annotation.GenericErrorApiResponse;
import net.gb.knox.gatekeeper.annotation.UserNotFoundApiResponse;
import net.gb.knox.gatekeeper.dto.CreateUserRequestDTO;
import net.gb.knox.gatekeeper.dto.UpdateUserRequestDTO;
import net.gb.knox.gatekeeper.dto.UserResponseDTO;
import net.gb.knox.gatekeeper.exception.UserNotFoundException;
import net.gb.knox.gatekeeper.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Operation(summary = "Create a new user.")
    @ApiResponse(
            responseCode = "201",
            description = "Created user.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UserResponseDTO.class)
            )
    )
    @GenericErrorApiResponse
    @PostMapping()
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody CreateUserRequestDTO createUserRequestDTO) throws URISyntaxException {
        logger.info("ENTER createUser | username={}", createUserRequestDTO.username());
        var userResponseDTO = userService.createUser(createUserRequestDTO);

        logger.info("EXIT createUser | username={} id={}", userResponseDTO.username(), userResponseDTO.id());
        return ResponseEntity
                .created(new URI("/users/" + userResponseDTO.id()))
                .body(userResponseDTO);
    }

    @Operation(
            summary = "Get a user by id.",
            security = @SecurityRequirement(name = "jwt")
    )
    @ApiResponse(
            responseCode = "200",
            description = "Got user.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UserResponseDTO.class)
            )
    )
    @UserNotFoundApiResponse
    @ForbiddenApiResponse
    @GenericErrorApiResponse
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUser(@PathVariable Long id) throws UserNotFoundException {
        logger.info("ENTER getUser | id={}", id);
        var userResponseDTO = userService.getUser(id);

        logger.info("EXIT getUser | id={}", userResponseDTO.id());
        return ResponseEntity.ok(userResponseDTO);
    }

    @Operation(
            summary = "Update a user's properties.",
            description = "The username and/or password can be updated via this endpoint.",
            security = @SecurityRequirement(name = "jwt")
    )
    @ApiResponse(
            responseCode = "200",
            description = "Updated user.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UserResponseDTO.class)
            )
    )
    @UserNotFoundApiResponse
    @ForbiddenApiResponse
    @GenericErrorApiResponse
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable Long id, @Valid @RequestBody UpdateUserRequestDTO updateUserRequestDTO) throws UserNotFoundException {
        logger.info("ENTER updateUser | id={}", id);
        var userResponseDTO = userService.updateUser(id, updateUserRequestDTO);

        logger.info("EXIT updateUser | id={}", userResponseDTO.id());
        return ResponseEntity.ok(userResponseDTO);
    }

    @Operation(
            summary = "Delete a user by id.",
            security = @SecurityRequirement(name = "jwt")
    )
    @ApiResponse(
            responseCode = "204",
            description = "Deleted user.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UserResponseDTO.class)
            )
    )
    @UserNotFoundApiResponse
    @ForbiddenApiResponse
    @GenericErrorApiResponse
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) throws UserNotFoundException {
        logger.info("ENTER deleteUser | id={}", id);
        userService.deleteUser(id);

        logger.info("EXIT deleteUser | id={}", id);
        return ResponseEntity.noContent().build();
    }
}
