package dev.tymurkulivar.robankApi.repositories;

import dev.tymurkulivar.robankApi.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
}
