package dev.tymurkulivar.robank_api.controllers;

import dev.tymurkulivar.robank_api.dto.PreferencesDTO;
import dev.tymurkulivar.robank_api.entities.RobankUser;
import dev.tymurkulivar.robank_api.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> createUser(@RequestBody RobankUser user) {
        try {
            userService.registerUser(user);
            return ResponseEntity.ok(Map.of("message", "User created"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/preferences")
    public ResponseEntity<?> getUserPreferences(@AuthenticationPrincipal User authenticatedUser) {
        try {
            PreferencesDTO preferences = userService.getUserPreferences(authenticatedUser.getUsername());
            return ResponseEntity.ok(preferences);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @PatchMapping("/preferences")
    public ResponseEntity<?> updateUserPreferences(@AuthenticationPrincipal User authenticatedUser,
                                                   @RequestBody Map<String, Object> updates) {
        try {
            userService.updateUserPreferences(authenticatedUser.getUsername(), updates);
            return ResponseEntity.ok(Map.of("message", "User updated successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getUserProfile(@AuthenticationPrincipal User authenticatedUser) {
        try {
            RobankUser user = userService.getUserProfile(authenticatedUser.getUsername());
            return ResponseEntity.ok(user);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
}