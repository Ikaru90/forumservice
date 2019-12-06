package com.forumservice.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.forumservice.domain.Role;
import com.forumservice.domain.User;
import com.forumservice.domain.Views;
import com.forumservice.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@RequestMapping("api/auth/")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private PasswordEncoder passwordEncoder;
    private final UserRepo userRepo;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, UserRepo userRepo, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("login")
    @JsonView(Views.ShortUser.class)
    public ResponseEntity login(@RequestBody User user) {
        try {
            String username = user.getUsername();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, user.getPassword()));
            User userFromDb = userRepo.findByUsername(user.getUsername());

            if (user == null) {
                throw new UsernameNotFoundException("User with username: " + username + " not found" );
            }

            return ResponseEntity.ok(userFromDb);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    @PostMapping("userInfo")
    @JsonView(Views.ShortUser.class)
    public User getUserInfo(@RequestBody User user) {
        return userRepo.findByUsername(user.getUsername());
    }

    @PostMapping("registration")
    @JsonView(Views.ShortUser.class)
    public User registration(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        return userRepo.save(user);
    }
}
