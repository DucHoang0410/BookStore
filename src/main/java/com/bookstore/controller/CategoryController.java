package com.bookstore.controller;

import com.bookstore.entity.Category;
import com.bookstore.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    // Tạo mới category (chỉ cho phép sau khi đăng nhập)
    @PostMapping("/create")
    public ResponseEntity<Category> createCategory(@RequestParam String name, Authentication authentication) {
        if (authentication != null) {
            Category category = categoryService.createCategory(name);
            return ResponseEntity.ok(category);
        } else {
            return ResponseEntity.status(403).body(null);
        }
    }

    // Sửa category
    @PutMapping("/update/{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable Long id, @RequestParam String name, Authentication authentication) {
        if (authentication != null) {
            Category updatedCategory = categoryService.updateCategory(id, name);
            if (updatedCategory != null) {
                return ResponseEntity.ok(updatedCategory);
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.status(403).body(null);
        }
    }

    // Xóa category
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id, Authentication authentication) {
        if (authentication != null) {
            boolean isDeleted = categoryService.deleteCategory(id);
            if (isDeleted) {
                return ResponseEntity.ok("Category deleted successfully");
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.status(403).body("Unauthorized");
        }
    }

    // Lấy tất cả categories (có thể không cần đăng nhập, tùy thuộc vào nhu cầu)
    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    // Lấy thông tin chi tiết của một category theo ID
    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long id) {
        Category category = categoryService.getCategoryById(id);
        if (category != null) {
            return ResponseEntity.ok(category);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
