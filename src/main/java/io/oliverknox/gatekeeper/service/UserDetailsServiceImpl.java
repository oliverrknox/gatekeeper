package io.oliverknox.gatekeeper.service;

import io.oliverknox.gatekeeper.model.UserModel;
import io.oliverknox.gatekeeper.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("ENTER loadUserByUsername | username={}", username);
        UserModel userModel = userRepository.findByUsername(username);

        if (userModel == null) {
            throw new UsernameNotFoundException("No user found.");
        }

        logger.info("ENTER loadUserByUsername | username={}", userModel.getUsername());
        return new User(userModel.getUsername(), userModel.getPasswordHash(), new ArrayList<>());
    }
}
