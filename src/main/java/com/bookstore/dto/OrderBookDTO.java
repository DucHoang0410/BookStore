package com.bookstore.dto;

import java.time.LocalDateTime;

public class OrderBookDTO {

    private Long bookId;
    private String title;
    private double price;
    private int quantity;
    private LocalDateTime orderDate;

    // Constructor
    public OrderBookDTO(Long bookId, String title, double price, int quantity, LocalDateTime orderDate) {
        this.bookId = bookId;
        this.title = title;
        this.price = price;
        this.quantity = quantity;
        this.orderDate = orderDate;
    }

    // Getters và Setters
    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }
}
