package dev.tymurkulivar.robank_backend.repositories;

import dev.tymurkulivar.robank_backend.entities.RobankUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RobankUserRepository extends JpaRepository<RobankUser, String> {
}
