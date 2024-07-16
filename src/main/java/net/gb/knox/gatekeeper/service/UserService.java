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
    private UserRepository repository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public CreateUserResponseDTO createUser(CreateUserRequestDTO request) {
        var hash = passwordEncoder.encode(request.password());
        var user = new UserModel(request.username(), hash);

        var newUser = repository.save(user);

        return new CreateUserResponseDTO(newUser.getId(), newUser.getUsername());
    }
}
