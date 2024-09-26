package com.bookstore.repository;

import com.bookstore.entity.Book;
import com.bookstore.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    // Truy vấn sách theo category
    List<Book> findByCategory(Category category);
    // Tìm sách theo tên sách chứa từ khóa (không phân biệt hoa/thường)
    List<Book> findByTitleContainingIgnoreCase(String title);
}

