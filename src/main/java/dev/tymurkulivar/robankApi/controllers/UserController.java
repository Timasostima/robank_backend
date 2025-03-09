package dev.tymurkulivar.robankApi.controllers;

import com.google.firebase.auth.FirebaseToken;
import dev.tymurkulivar.robankApi.Entities.User;
import dev.tymurkulivar.robankApi.repositories.UserRepository;
import dev.tymurkulivar.robankApi.services.FirebaseAuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        FirebaseToken decodedToken = firebaseAuthService.verifyToken(token.replace("Bearer ", ""));
        if (decodedToken == null) {
            return ResponseEntity.status(401).body("Invalid Token");
        }

        User user = new User(decodedToken.getUid(), decodedToken.getEmail(), decodedToken.getName());
        userRepository.save(user);
        return ResponseEntity.ok(user);
    }
}