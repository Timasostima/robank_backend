package dev.tymurkulivar.robank_api.controllers;

import dev.tymurkulivar.robank_api.dto.CategoryDTO;
import dev.tymurkulivar.robank_api.services.CategoryService;
import org.springframework.security.core.userdetails.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<?> getCategories(@AuthenticationPrincipal User authUser) {
        try {
            return ResponseEntity.ok(categoryService.getCategories(authUser.getUsername()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> createGoal(@RequestBody CategoryDTO categoryDTO, @AuthenticationPrincipal User authUser) {
        try {
            CategoryDTO category = categoryService.createCategory(categoryDTO, authUser.getUsername());
            return ResponseEntity.ok(category);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

