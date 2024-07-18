package net.gb.knox.gatekeeper.service;

import net.gb.knox.gatekeeper.dto.CreateUserRequestDTO;
import net.gb.knox.gatekeeper.dto.UpdateUserRequestDTO;
import net.gb.knox.gatekeeper.dto.UserResponseDTO;
import net.gb.knox.gatekeeper.exception.UserNotFoundException;
import net.gb.knox.gatekeeper.model.UserModel;
import net.gb.knox.gatekeeper.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserResponseDTO createUser(CreateUserRequestDTO createUserRequestDTO) {
        var passwordHash = passwordEncoder.encode(createUserRequestDTO.password());
        var userModel = new UserModel(createUserRequestDTO.username(), passwordHash);

        var savedUser = userRepository.save(userModel);

        return new UserResponseDTO(savedUser.getId(), savedUser.getUsername());
    }

    public UserResponseDTO getUser(Long id) throws UserNotFoundException {
        var existingUser = userRepository.findById(id).orElseThrow(() ->
                new UserNotFoundException("No user found.", Map.of("Id", "No user with Id = " + id + "."))
        );
        return new UserResponseDTO(existingUser.getId(), existingUser.getUsername());
    }

    public UserResponseDTO updateUser(Long id, UpdateUserRequestDTO updateUserRequestDTO) throws UserNotFoundException {
        var existingUser = userRepository.findById(id).orElseThrow(() ->
                new UserNotFoundException("No user found.", Map.of("Id", "No user with Id = " + id + "."))
        );
        var username = updateUserRequestDTO.username() == null
                ? existingUser.getUsername()
                : updateUserRequestDTO.username();
        var passwordHash = updateUserRequestDTO.password() == null
                ? existingUser.getPasswordHash()
                : passwordEncoder.encode(updateUserRequestDTO.password());

        var updatedUser = new UserModel(id, username, passwordHash);
        var savedUser = userRepository.save(updatedUser);

        return new UserResponseDTO(savedUser.getId(), savedUser.getUsername());
    }

    public void deleteUser(Long id) throws UserNotFoundException {
        userRepository.findById(id).orElseThrow(() ->
                new UserNotFoundException("No user found.", Map.of("Id", "No user with Id = " + id + "."))
        );
        userRepository.deleteById(id);
    }
}
