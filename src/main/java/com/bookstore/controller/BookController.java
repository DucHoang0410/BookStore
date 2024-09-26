package com.bookstore.controller;

import com.bookstore.entity.Book;
import com.bookstore.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BookService bookService;

    // Thêm sách mới
    @PostMapping("/create")
    public ResponseEntity<Book> createBook(@RequestBody Book book) {
        Book createdBook = bookService.addBook(book);
        return ResponseEntity.ok(createdBook);
    }

    // Sửa sách
    @PutMapping("/update/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody Book bookDetails) {
        Book updatedBook = bookService.updateBook(id, bookDetails);
        if (updatedBook != null) {
            return ResponseEntity.ok(updatedBook);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Xóa sách
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable Long id) {
        boolean isDeleted = bookService.deleteBook(id);
        if (isDeleted) {
            return ResponseEntity.ok("Book deleted successfully.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Lấy danh sách sách theo Category
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<Book>> getBooksByCategory(@PathVariable Long categoryId) {
        List<Book> books = bookService.getBooksByCategory(categoryId);
        if (books != null) {
            return ResponseEntity.ok(books);
        } else {
            return ResponseEntity.notFound().build(); // Xử lý nếu category không tồn tại
        }
    }

    // Lấy sách theo ID
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        Book book = bookService.getBookById(id);
        if (book != null) {
            return ResponseEntity.ok(book);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    // Lấy tất cả sách
    @GetMapping("")
    public ResponseEntity<List<Book>> getAllBooks() {
        List<Book> books = bookService.getAllBooks();
        if (!books.isEmpty()) {
            return ResponseEntity.ok(books);
        } else {
            return ResponseEntity.noContent().build(); // Trả về 204 nếu không có sách
        }
    }
    // API tìm kiếm sách theo tên
    @GetMapping("/search")
    public ResponseEntity<List<Book>> searchBooksByTitle(@RequestParam String title) {
        List<Book> books = bookService.searchBooksByTitle(title);
        if (!books.isEmpty()) {
            return ResponseEntity.ok(books);
        } else {
            return ResponseEntity.noContent().build(); // Trả về 204 nếu không có sách
        }
    }

}
