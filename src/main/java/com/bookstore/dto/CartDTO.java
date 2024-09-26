package com.bookstore.dto;

import java.math.BigDecimal;
import java.util.List;

public class CartDTO {

    private Long id;
    private Long userId;
    private BigDecimal total;
    private List<CartItemDTO> items;

    // Constructors
    public CartDTO() {}

    public CartDTO(Long id, Long userId, BigDecimal total, List<CartItemDTO> items) {
        this.id = id;
        this.userId = userId;
        this.total = total;
        this.items = items;
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

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public List<CartItemDTO> getItems() {
        return items;
    }

    public void setItems(List<CartItemDTO> items) {
        this.items = items;
    }
}
