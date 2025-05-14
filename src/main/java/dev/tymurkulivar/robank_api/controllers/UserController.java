package dev.tymurkulivar.robank_api.controllers;

import dev.tymurkulivar.robank_api.dto.PreferencesDTO;
import dev.tymurkulivar.robank_api.entities.RobankUser;
import dev.tymurkulivar.robank_api.services.UserService;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/check-new-user")
    public ResponseEntity<?> checkNewUser(@RequestParam("userId") String userId) {
        try {
            boolean exists = userService.checkNewUser(userId);
            return ResponseEntity.ok(Map.of("exists", exists));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "An error occurred while checking the user"));
        }
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

    @PostMapping("/upload-pfp-firebase")
    public ResponseEntity<?> uploadPfpFromFirebase(
            @RequestBody Map<String, String> requestBody,
            @AuthenticationPrincipal User authUser) {
        try {
            String imageUrl = requestBody.get("imageUrl");
            String filename = userService.uploadUserImageFromUrl(authUser.getUsername(), imageUrl);
            return ResponseEntity.ok(Map.of("message", "Image uploaded from URL", "filename", filename));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Server error during image upload"));
        }
    }

    @PostMapping("/upload-pfp")
    public ResponseEntity<?> uploadUserProfileImage(
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal User authUser) {
        try {
            String filename = userService.uploadUserProfileImage(authUser.getUsername(), file);
            return ResponseEntity.ok(Map.of("message", "Profile image uploaded", "filename", filename));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Server error during image upload"));
        }
    }

    @GetMapping("/pfp")
    public ResponseEntity<?> getUserProfileImage(@AuthenticationPrincipal User authUser) {
        try {
            Resource image = userService.getUserProfileImage(authUser.getUsername());
            Path imagePath = Paths.get(image.getURI());
            String contentType = Files.probeContentType(imagePath);

            if (contentType == null) {
                contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(image);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Could not read image"));
        }
    }
}