package com.forumservice.controller;

import com.forumservice.model.Message;
import com.forumservice.model.Theme;
import com.forumservice.repository.ThemeRepository;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("api/theme")
public class ThemeController {
    private final ThemeRepository themeRepository;

    public ThemeController(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    @GetMapping
    public List<Theme> list() {
        return themeRepository.findAll();
    }

    @GetMapping("{id}")
    public Theme getOne(@PathVariable("id") Theme theme) {
        return theme;
    }

    @PostMapping
    public Theme create(@RequestBody Theme theme) {
        theme.setCreated(LocalDateTime.now());
        return themeRepository.save(theme);
    }

    @MessageMapping("/changeMessage")
    @SendTo("/topic/activity")
    public Theme change(Theme theme) {
        theme.setCreated(LocalDateTime.now());
        return themeRepository.save(theme);
    }
}
