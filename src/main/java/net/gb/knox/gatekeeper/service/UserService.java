package net.gb.knox.gatekeeper.service;

import net.gb.knox.gatekeeper.dto.CreateUserRequestDTO;
import net.gb.knox.gatekeeper.dto.CreateUserResponseDTO;
import net.gb.knox.gatekeeper.model.UserModel;
import net.gb.knox.gatekeeper.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public CreateUserResponseDTO createUser(CreateUserRequestDTO createUserRequestDTO) {
        var passwordHash = passwordEncoder.encode(createUserRequestDTO.password());
        var userModel = new UserModel(createUserRequestDTO.username(), passwordHash);

        var savedUser = userRepository.save(userModel);

        return new CreateUserResponseDTO(savedUser.getId(), savedUser.getUsername());
    }
}
