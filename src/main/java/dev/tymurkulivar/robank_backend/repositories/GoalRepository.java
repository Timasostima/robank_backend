package dev.tymurkulivar.robank_backend.repositories;

import dev.tymurkulivar.robank_backend.entities.Goal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GoalRepository extends JpaRepository<Goal, Long> {
    List<Goal> findByUserUidOrderByIndex(String uid);
}