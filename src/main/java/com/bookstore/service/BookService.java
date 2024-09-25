package com.bookstore.service;

import com.bookstore.entity.Book;
import com.bookstore.entity.Category;
import com.bookstore.repository.BookRepository;
import com.bookstore.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    // Thêm sách mới
    public Book addBook(Book book) {
        // Kiểm tra và lấy thông tin đầy đủ của Category từ cơ sở dữ liệu
        if (book.getCategory() != null && book.getCategory().getId() != null) {
            Category category = categoryRepository.findById(book.getCategory().getId()).orElse(null);
            book.setCategory(category); // Gán category đầy đủ cho book
        }
        return bookRepository.save(book);
    }

    // Sửa sách
    public Book updateBook(Long id, Book bookDetails) {
        Optional<Book> bookOptional = bookRepository.findById(id);
        if (bookOptional.isPresent()) {
            Book book = bookOptional.get();
            book.setTitle(bookDetails.getTitle());
            book.setAuthor(bookDetails.getAuthor());
            book.setDescription(bookDetails.getDescription());
            book.setPrice(bookDetails.getPrice());
            book.setStock(bookDetails.getStock());

            if (bookDetails.getCategory() != null && bookDetails.getCategory().getId() != null) {
                Category category = categoryRepository.findById(bookDetails.getCategory().getId()).orElse(null);
                book.setCategory(category); // Gán category đầy đủ cho book
            }

            return bookRepository.save(book);
        }
        return null; // Xử lý nếu không tìm thấy sách
    }

    // Xóa sách
    public boolean deleteBook(Long id) {
        Optional<Book> bookOptional = bookRepository.findById(id);
        if (bookOptional.isPresent()) {
            bookRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Lấy tất cả sách
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    // Lấy sách theo ID
    public Book getBookById(Long id) {
        return bookRepository.findById(id).orElse(null);
    }
}
