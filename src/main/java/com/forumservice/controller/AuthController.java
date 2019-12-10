package com.forumservice.controller;

import com.forumservice.model.User;
import com.forumservice.security.jwt.JwtTokenProvider;
import com.forumservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/auth/")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthController(
            AuthenticationManager authenticationManager,
            JwtTokenProvider jwtTokenProvider,
            UserService userService,
            PasswordEncoder passwordEncoder
    ) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("login")
    public ResponseEntity login(@RequestBody User user) {
        try {
            String username = user.getUsername();

            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, user.getPassword()));
            User userFromDb = userService.findByUsername(username);

            if (userFromDb == null) {
                throw new UsernameNotFoundException("User with username: " + username + " not found");
            }

            String token = jwtTokenProvider.createToken(username, userFromDb.getRoles());

            Map<Object, Object> response = new HashMap<>();
            response.put("username", userFromDb.getUsername());
            response.put("firstName", userFromDb.getFirstName());
            response.put("lastName", userFromDb.getLastName());
            response.put("token", token);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<Object, Object> response = new HashMap<>();
            response.put("errorMessage", "Incorrect username or password");

                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(response);
        }
    }

    @PostMapping("registration")
    public ResponseEntity registration(@RequestBody User user) {
        try {
            String password = user.getPassword();
            User registeredUser = userService.register(user);

            String username = user.getUsername();

            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

            User userFromDb = userService.findByUsername(username);

            if (userFromDb == null) {
                throw new UsernameNotFoundException("User with username: " + username + " not found");
            }

            String token = jwtTokenProvider.createToken(username, userFromDb.getRoles());

            Map<Object, Object> response = new HashMap<>();
            response.put("username", registeredUser.getUsername());
            response.put("firstName", registeredUser.getFirstName());
            response.put("lastName", registeredUser.getLastName());
            response.put("token", token);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<Object, Object> response = new HashMap<>();
            response.put("errorMessage", "Internal server error");

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(response);
        }
    }
}
