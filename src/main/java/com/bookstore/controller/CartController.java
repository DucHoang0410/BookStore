package com.bookstore.controller;

import com.bookstore.dto.CartDTO;
import com.bookstore.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    // Thêm sách vào giỏ hàng
    @PostMapping("/add/{userId}/{bookId}")
    public ResponseEntity<CartDTO> addToCart(
            @PathVariable Long userId,
            @PathVariable Long bookId,
            @RequestParam int quantity) {
        try {
            CartDTO cart = cartService.addToCart(userId, bookId, quantity);
            return ResponseEntity.ok(cart);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    // Xóa sách khỏi giỏ hàng
    @DeleteMapping("/remove/{userId}/{bookId}")
    public ResponseEntity<CartDTO> removeFromCart(
            @PathVariable Long userId,
            @PathVariable Long bookId) {
        try {
            CartDTO cart = cartService.removeFromCart(userId, bookId);
            return ResponseEntity.ok(cart);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // Lấy giỏ hàng của người dùng
    @GetMapping("/{userId}")
    public ResponseEntity<CartDTO> getCart(@PathVariable Long userId) {
        try {
            CartDTO cart = cartService.getCartByUserId(userId);
            return ResponseEntity.ok(cart);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // Cập nhật số lượng sách trong giỏ hàng
    @PutMapping("/update/{userId}/{bookId}")
    public ResponseEntity<CartDTO> updateCartItemQuantity(
            @PathVariable Long userId,
            @PathVariable Long bookId,
            @RequestParam int quantity) {
        try {
            CartDTO cart = cartService.updateCartItemQuantity(userId, bookId, quantity);
            return ResponseEntity.ok(cart);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}
