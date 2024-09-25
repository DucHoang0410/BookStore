package com.bookstore.dto;

import java.time.LocalDateTime;

public class OrderDTO {

    private Long id;
    private Long userId;
    private LocalDateTime orderDate;
    private double total;
    private String status;

    // Constructors
    public OrderDTO() {}

    public OrderDTO(Long id, Long userId, LocalDateTime orderDate, double total, String status) {
        this.id = id;
        this.userId = userId;
        this.orderDate = orderDate;
        this.total = total;
        this.status = status;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

