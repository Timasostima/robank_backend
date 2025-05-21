package dev.tymurkulivar.robank_backend.controllers;

import dev.tymurkulivar.robank_backend.dto.GoalDTO;
import dev.tymurkulivar.robank_backend.entities.Goal;
import dev.tymurkulivar.robank_backend.services.GoalService;
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
@RequestMapping("/goals")
public class GoalController {

    private final GoalService goalService;

    public GoalController(GoalService goalService) {
        this.goalService = goalService;
    }

    @GetMapping
    public ResponseEntity<?> getGoals(@AuthenticationPrincipal User authUser) {
        try {
            return ResponseEntity.ok(goalService.getGoals(authUser.getUsername()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> createGoal(@RequestBody GoalDTO goalDTO, @AuthenticationPrincipal User authUser) {
        try {
            GoalDTO goal = goalService.createGoal(goalDTO, authUser.getUsername());
            return ResponseEntity.ok(goal);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateGoal(@PathVariable Long id, @RequestBody GoalDTO goalDTO, @AuthenticationPrincipal User authUser) {
        try {
            Goal updatedGoal = goalService.updateGoal(id, goalDTO, authUser.getUsername());
            return ResponseEntity.ok(updatedGoal);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteGoal(@PathVariable Long id, @AuthenticationPrincipal User authUser) {
        try {
            goalService.deleteGoal(id, authUser.getUsername());
            return ResponseEntity.ok(Map.of("message", "Goal deleted successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{id}/image")
    public ResponseEntity<?> uploadGoalImage(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal User authUser) {
        try {
            String filename = goalService.uploadGoalImage(id, authUser.getUsername(), file);
            return ResponseEntity.ok(Map.of("message", "Goal image uploaded", "filename", filename));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Server error during image upload"));
        }
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<?> getGoalImage(
            @PathVariable Long id,
            @AuthenticationPrincipal User authUser) {
        try {
            Resource image = goalService.getGoalImage(id, authUser.getUsername());
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