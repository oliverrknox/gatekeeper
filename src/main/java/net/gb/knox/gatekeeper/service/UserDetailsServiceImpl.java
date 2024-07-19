package net.gb.knox.gatekeeper.service;

import net.gb.knox.gatekeeper.model.UserModel;
import net.gb.knox.gatekeeper.repository.UserRepository;
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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserModel userModel = userRepository.findByUsername(username);

        if (userModel == null) {
            throw new UsernameNotFoundException("No user found.");
        }

        return new User(userModel.getUsername(), userModel.getPasswordHash(), new ArrayList<>());
    }
}
