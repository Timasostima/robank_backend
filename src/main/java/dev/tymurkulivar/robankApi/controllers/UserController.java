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
    public ResponseEntity<?> registerUser(@RequestHeader("Authorization") String token) {
        // Remove "Bearer " prefix
        FirebaseToken decodedToken = firebaseAuthService.verifyToken(token.replace("Bearer ", ""));
        if (decodedToken == null) {
            return ResponseEntity.status(401).body("Invalid Token");
        }
        System.out.println("uid: " + decodedToken.getUid());

        // Check if user already exists
//        if (userRepository.existsById(decodedToken.getUid())) {
//            return ResponseEntity.ok("User already registered.");
//        }

        // Create and save user
        User user = new User(decodedToken.getUid(), decodedToken.getEmail(), decodedToken.getName());
        userRepository.save(user);

        return ResponseEntity.ok(user);
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getUserProfile(@AuthenticationPrincipal User user) {
        if (user == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }
        return ResponseEntity.ok("Authenticated user: " + user.getEmail());
    }
}