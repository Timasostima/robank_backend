package dev.tymurkulivar.robankApi.controllers;

import com.google.firebase.auth.FirebaseToken;
import dev.tymurkulivar.robankApi.Entities.User;
import dev.tymurkulivar.robankApi.repositories.UserRepository;
import dev.tymurkulivar.robankApi.services.FirebaseAuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserRepository userRepository;
    private final FirebaseAuthService firebaseAuthService;

    public UserController(UserRepository userRepository, FirebaseAuthService firebaseAuthService) {
        this.userRepository = userRepository;
        this.firebaseAuthService = firebaseAuthService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> createUser(@RequestBody User user) {
        if (user == null) {
            return ResponseEntity.badRequest().body("User is required");
        }
        if (userRepository.existsById(user.getUid())) {
            return ResponseEntity.badRequest().body("User already exists");
        }

        userRepository.save(user);
        return ResponseEntity.ok("User created");
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getUserProfile(@AuthenticationPrincipal User user) {
        if (user == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }
        return ResponseEntity.ok("Authenticated user: " + user.getEmail());
    }
}