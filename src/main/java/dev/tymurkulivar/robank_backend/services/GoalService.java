package dev.tymurkulivar.robank_backend.services;

import dev.tymurkulivar.robank_backend.dto.GoalDTO;
import dev.tymurkulivar.robank_backend.entities.Bill;
import dev.tymurkulivar.robank_backend.entities.Goal;
import dev.tymurkulivar.robank_backend.entities.RobankUser;
import dev.tymurkulivar.robank_backend.repositories.GoalRepository;
import dev.tymurkulivar.robank_backend.repositories.RobankUserRepository;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class GoalService {
    private final GoalRepository goalRepository;
    private final RobankUserRepository userRepository;

    public GoalService(GoalRepository goalRepository, RobankUserRepository userRepository) {
        this.goalRepository = goalRepository;
        this.userRepository = userRepository;
    }

    public List<GoalDTO> getGoals(String userId) {
        List<Goal> goals = goalRepository.findByUserUidOrderByIndex(userId);
        List<GoalDTO> goalDTOs = new ArrayList<>();
        for (Goal goal : goals) {
            goalDTOs.add(new GoalDTO(goal)); // Automatically includes id
        }
        return goalDTOs;
    }

    public GoalDTO createGoal(GoalDTO goalDTO, String userId) {
        RobankUser user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Goal goal = new Goal(goalDTO);
        goal.setUser(user);
        goalRepository.save(goal);
        return new GoalDTO(goal);
    }

    public Goal updateGoal(Long id, GoalDTO goalDTO, String userId) {
        Goal goal = validateAndFetchGoal(id, userId);
        updateGoalFromDTO(goal, goalDTO);
        return goalRepository.save(goal);
    }

    public Goal validateAndFetchGoal(Long id, String userId) {
        Goal goal = goalRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Goal not found"));
        if (!goal.getUser().getUid().equals(userId)) {
            throw new IllegalArgumentException("Unauthorized access to goal");
        }
        return goal;
    }

    public void updateGoalFromDTO(Goal goal, GoalDTO dto) {
        if (dto.getName() != null) {
            goal.setName(dto.getName());
        }
        if (dto.getPrice() != null) {
            goal.setPrice(dto.getPrice());
        }
        if (dto.getIndex() != null) {
            goal.setIndex(dto.getIndex());
        }
    }

    public void deleteGoal(Long id, String userId) {
        Goal goal = validateAndFetchGoal(id, userId);
        goalRepository.delete(goal);
    }

    public String uploadGoalImage(Long goalId, String userId, MultipartFile file) {
        Goal goal = goalRepository.findById(goalId)
                .orElseThrow(() -> new IllegalArgumentException("Goal not found"));

        if (!goal.getUser().getUid().equals(userId)) {
            throw new IllegalArgumentException("You don't have permission to modify this goal");
        }

        try {
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf('.') + 1).toLowerCase();

            if (!List.of("png", "jpg", "jpeg", "webp").contains(extension)) {
                throw new IllegalArgumentException("Unsupported file type");
            }

            String filename = "goal_" + goalId + "_" + UUID.randomUUID() + "." + extension;
            Path uploadDir = Paths.get("uploads/goals/");

            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            Path filePath = uploadDir.resolve(filename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            goal.setImageUrl(filename);
            goalRepository.save(goal);

            return filename;

        } catch (IOException e) {
            throw new RuntimeException("Image upload failed", e);
        }
    }

    public Resource getGoalImage(Long goalId, String userId) throws IOException {
        Goal goal = goalRepository.findById(goalId)
                .orElseThrow(() -> new IllegalArgumentException("Goal not found"));

        if (!goal.getUser().getUid().equals(userId)) {
            throw new IllegalArgumentException("You don't have permission to access this goal");
        }

        String filename = goal.getImageUrl();
        if (filename == null || filename.isEmpty()) {
            throw new IllegalArgumentException("No image found for this goal");
        }

        Path imagePath = Paths.get("uploads/goals/").resolve(filename);
        if (!Files.exists(imagePath)) {
            throw new IllegalArgumentException("Image file not found");
        }

        return new UrlResource(imagePath.toUri());
    }
}