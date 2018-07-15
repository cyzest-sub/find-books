package com.cyzest.findbooks.service;

import com.cyzest.findbooks.model.DefaultAuthUser;
import com.cyzest.findbooks.dao.User;
import com.cyzest.findbooks.dao.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder;

    @Transactional
    public void registerUser(String id, String password) throws Exception {

        if (userRepository.exists(id)) {
            throw new IllegalAccessException();
        }

        User user = new User();

        user.setId(id);
        user.setPassword(passwordEncoder.encode(password));
        user.setRegDate(new Date());

        userRepository.saveAndFlush(user);
    }

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        return Optional.ofNullable(userRepository.findById(id))
                .map(user -> new DefaultAuthUser(user))
                .orElseThrow(() -> new UsernameNotFoundException(id + " not found"));
    }

}
