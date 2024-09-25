package com.bookstore.service;

import com.bookstore.entity.Category;
import com.bookstore.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    // Thêm mới category
    public Category createCategory(String name) {
        Category category = new Category();
        category.setName(name);
        return categoryRepository.save(category);
    }

    // Sửa category
    public Category updateCategory(Long id, String name) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        if (optionalCategory.isPresent()) {
            Category category = optionalCategory.get();
            category.setName(name);
            return categoryRepository.save(category);
        }
        return null; // Hoặc xử lý nếu không tìm thấy
    }

    // Xóa category
    public boolean deleteCategory(Long id) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        if (optionalCategory.isPresent()) {
            categoryRepository.deleteById(id);
            return true;
        }
        return false; // Hoặc xử lý nếu không tìm thấy
    }

    // Lấy tất cả categories
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    // Lấy category theo ID
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElse(null);
    }
}
