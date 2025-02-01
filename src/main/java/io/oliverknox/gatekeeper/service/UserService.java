package io.oliverknox.gatekeeper.service;

import io.oliverknox.gatekeeper.dto.CreateUserRequestDTO;
import io.oliverknox.gatekeeper.dto.UpdateUserRequestDTO;
import io.oliverknox.gatekeeper.dto.UserResponseDTO;
import io.oliverknox.gatekeeper.exception.UserNotFoundException;
import io.oliverknox.gatekeeper.model.UserModel;
import io.oliverknox.gatekeeper.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public UserResponseDTO createUser(CreateUserRequestDTO createUserRequestDTO) {
        logger.info("ENTER createUser | username={}", createUserRequestDTO.username());
        var passwordHash = passwordEncoder.encode(createUserRequestDTO.password());
        var userModel = new UserModel(createUserRequestDTO.username(), passwordHash);

        var savedUser = userRepository.save(userModel);

        logger.info("EXIT createUser | username={} id={}", savedUser.getUsername(), savedUser.getId());
        return new UserResponseDTO(savedUser.getId(), savedUser.getUsername());
    }

    public UserResponseDTO getUser(Long id) throws UserNotFoundException {
        logger.info("ENTER getUser | id={}", id);
        var existingUser = userRepository.findById(id).orElseThrow(() ->
                new UserNotFoundException("No user found.", Map.of("Id", "No user with Id = " + id + "."))
        );

        logger.info("EXIT getUser | id={}", existingUser.getId());
        return new UserResponseDTO(existingUser.getId(), existingUser.getUsername());
    }

    public UserResponseDTO updateUser(Long id, UpdateUserRequestDTO updateUserRequestDTO) throws UserNotFoundException {
        logger.info("ENTER updateUser | id={}", id);
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

        logger.info("EXIT updateUser | id={}", savedUser.getId());
        return new UserResponseDTO(savedUser.getId(), savedUser.getUsername());
    }

    public void deleteUser(Long id) throws UserNotFoundException {
        logger.info("ENTER deleteUser | id={}", id);
        userRepository.findById(id).orElseThrow(() ->
                new UserNotFoundException("No user found.", Map.of("Id", "No user with Id = " + id + "."))
        );
        userRepository.deleteById(id);
        logger.info("ENTER deleteUser | id={}", id);
    }
}
