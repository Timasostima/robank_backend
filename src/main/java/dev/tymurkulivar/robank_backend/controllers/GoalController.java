package dev.tymurkulivar.robank_backend.controllers;

import dev.tymurkulivar.robank_backend.dto.GoalDTO;
import dev.tymurkulivar.robank_backend.entities.Goal;
import dev.tymurkulivar.robank_backend.services.GoalService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

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
}