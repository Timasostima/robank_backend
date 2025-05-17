package dev.tymurkulivar.robank_backend.repositories;

import dev.tymurkulivar.robank_backend.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findAllByUserUid(String userUid);
}