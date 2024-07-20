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
        var userResponseDTO = userService.createUser(createUserRequestDTO);
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
        var userResponseDTO = userService.getUser(id);
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
        var userResponseDTO = userService.updateUser(id, updateUserRequestDTO);
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
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
