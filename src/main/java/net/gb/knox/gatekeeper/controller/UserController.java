package net.gb.knox.gatekeeper.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import net.gb.knox.gatekeeper.dto.CreateUserRequestDTO;
import net.gb.knox.gatekeeper.dto.ErrorResponseDTO;
import net.gb.knox.gatekeeper.dto.UserResponseDTO;
import net.gb.knox.gatekeeper.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(summary = "Create a new user.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Created user.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Generic error.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class)
                    )
            )
    })
    @PostMapping()
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody CreateUserRequestDTO createUserRequestDTO) throws URISyntaxException {
        var createUserResponseDTO = userService.createUser(createUserRequestDTO);
        return ResponseEntity
                .created(new URI("/users/" + createUserResponseDTO.id()))
                .body(createUserResponseDTO);
    }
}
