package com.forumservice.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.forumservice.domain.User;
import com.forumservice.domain.Views;
import com.forumservice.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth")
public class AuthorizationController {
    private final UserRepo userRepo;

    @Autowired
    public AuthorizationController(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @PostMapping("/login")
    @JsonView(Views.UserWithoutPassword.class)
    public User login(@RequestBody User user) {
        User userFromDb = userRepo.findByUsername(user.getUsername());
        if (userFromDb.getPassword().equals(user.getPassword())) {
            return userFromDb;
        }
        return null;
    }

    @PostMapping("/userInfo")
    @JsonView(Views.UserWithoutPassword.class)
    public User getUserInfo(@RequestBody User user) {
        return userRepo.findByUsername(user.getUsername());
    }

    @PostMapping("/registration")
    @JsonView(Views.UserWithoutPassword.class)
    public User registration(@RequestBody User user) {
        return userRepo.save(user);
    }
}
