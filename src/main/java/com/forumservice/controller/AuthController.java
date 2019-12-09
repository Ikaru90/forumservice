package com.forumservice.controller;

import com.forumservice.model.User;
import com.forumservice.repository.UserRepository;
import com.forumservice.security.jwt.JwtTokenProvider;
import com.forumservice.service.UserService;
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

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/auth/")
public class AuthController {
    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;

    private final UserService userService;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
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
            response.put("username", username);
            response.put("token", token);

            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }

//    @PostMapping("login")
//    public ResponseEntity login(@RequestBody User user) {
//        try {
//            String username = user.getUsername();
//            Authentication token = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, user.getPassword()));
//            User userFromDb = userRepo.findByUsername(user.getUsername());
//
//            if (user == null) {
//                throw new UsernameNotFoundException("User with username: " + username + " not found" );
//            }
//
//            return ResponseEntity.ok(token);
//        } catch (AuthenticationException e) {
//            throw new BadCredentialsException("Invalid username or password");
//        }
//    }

//    @PostMapping("userInfo")
//    public User getUserInfo(@RequestBody User user) {
//        return userRepo.findByUsername(user.getUsername());
//    }
//
//    @PostMapping("registration")
//    public User registration(@RequestBody User user) {
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        user.setActive(true);
//        user.setRoles(Collections.singleton(Role.USER));
//        return userRepo.save(user);
//    }
}
