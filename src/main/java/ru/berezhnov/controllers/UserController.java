package ru.berezhnov.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.berezhnov.dto.UserDTO;
import ru.berezhnov.util.EmailExtractor;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final EmailExtractor emailExtractor;

    @Autowired
    public UserController(EmailExtractor emailExtractor) {
        this.emailExtractor = emailExtractor;
    }

    @GetMapping
    public ResponseEntity<UserDTO> getUserDetails(@RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.ok(emailExtractor.getUserFromHeader(authHeader));
    }
}
