package dev.tymurkulivar.robank_api.controllers;

import dev.tymurkulivar.robank_api.dto.PreferencesDTO;
import dev.tymurkulivar.robank_api.repositories.RobankUserRepository;
import org.springframework.security.core.userdetails.User;
import dev.tymurkulivar.robank_api.entities.RobankUser;
import dev.tymurkulivar.robank_api.services.FirebaseAuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    private final RobankUserRepository userRepository;
    private final FirebaseAuthService firebaseAuthService;

    public UserController(RobankUserRepository userRepository, FirebaseAuthService firebaseAuthService) {
        this.userRepository = userRepository;
        this.firebaseAuthService = firebaseAuthService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> createUser(@RequestBody RobankUser user) {
        if (user == null) {
            return ResponseEntity.badRequest().body("User is required");
        }
        if (userRepository.existsById(user.getUid())) {
            return ResponseEntity.badRequest().body("User already exists");
        }

        userRepository.save(user);
        return ResponseEntity.ok(Map.of("message", "User created"));
    }

    @GetMapping("/preferences")
    public ResponseEntity<?> getUserAttributes(@AuthenticationPrincipal User authenticatedUser) {
        if (authenticatedUser == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        String uid = authenticatedUser.getUsername();
        RobankUser user = userRepository.findById(uid).orElse(null);

        if (user == null) {
            return ResponseEntity.status(404).body("User not found");
        }
        PreferencesDTO preferences = new PreferencesDTO(user.getPreferences());
        return ResponseEntity.ok(preferences);
    }

    @PatchMapping("/preferences")
    public ResponseEntity<?> updateUserPreferences(@AuthenticationPrincipal User authenticatedUser,
                                                  @RequestBody Map<String, Object> updates) {
        if (authenticatedUser == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        String uid = authenticatedUser.getUsername();
        RobankUser user = userRepository.findById(uid).orElse(null);

        if (user == null) {
            return ResponseEntity.status(404).body("User not found");
        }

        updates.forEach((key, value) -> {
            switch (key) {
                case "language" -> user.getPreferences().setLanguage((String) value);
                case "currency" -> user.getPreferences().setCurrency((String) value);
                case "theme" -> user.getPreferences().setTheme((String) value);
                case "notifications" -> user.getPreferences().setNotifications(Boolean.parseBoolean((String) value));
                default -> throw new IllegalArgumentException("Invalid attribute: " + key);
            }
        });

        userRepository.save(user);
        return ResponseEntity.ok(Map.of("message", "User updated successfully"));
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getUserProfile(@AuthenticationPrincipal User authenticatedUser) {
        if (authenticatedUser == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        // Retrieve the user from the database using the UID
        String uid = authenticatedUser.getUsername(); // UID is stored as the username
        RobankUser user = userRepository.findById(uid).orElse(null);

        if (user == null) {
            return ResponseEntity.status(404).body("User not found");
        }

        return ResponseEntity.ok(user); // Return the full user profile
    }
}