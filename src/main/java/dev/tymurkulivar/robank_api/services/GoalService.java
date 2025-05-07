package dev.tymurkulivar.robank_api.services;

import dev.tymurkulivar.robank_api.dto.GoalDTO;
import dev.tymurkulivar.robank_api.entities.Goal;
import dev.tymurkulivar.robank_api.entities.RobankUser;
import dev.tymurkulivar.robank_api.repositories.GoalRepository;
import dev.tymurkulivar.robank_api.repositories.RobankUserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
}