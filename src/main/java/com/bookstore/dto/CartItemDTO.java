package com.bookstore.dto;

import java.math.BigDecimal;

public class CartItemDTO {

    private Long id;
    private Long bookId;
    private String bookTitle;
    private int quantity;
    private BigDecimal price;
    private String imageUrl; // Thêm trường imageUrl

    // Constructor
    public CartItemDTO(Long id, Long bookId, String bookTitle, int quantity, BigDecimal price, String imageUrl) {
        this.id = id;
        this.bookId = bookId;
        this.bookTitle = bookTitle;
        this.quantity = quantity;
        this.price = price;
        this.imageUrl = imageUrl; // Gán giá trị cho imageUrl
    }

    // Getters và Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
