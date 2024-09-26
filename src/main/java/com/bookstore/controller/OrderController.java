package com.bookstore.controller;

import com.bookstore.dto.OrderBookDTO;
import com.bookstore.dto.OrderDTO;
import com.bookstore.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    // API để tạo đơn hàng từ giỏ hàng (checkout)
    @PostMapping("/checkout/{userId}")
    public ResponseEntity<List<OrderBookDTO>> checkout(@PathVariable Long userId) {
        try {
            // Tạo đơn hàng và thanh toán luôn
            List<OrderBookDTO> purchasedItems = orderService.createOrderFromCart(userId);
            return ResponseEntity.ok(purchasedItems);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // API để lấy danh sách tất cả đơn hàng của người dùng
    @GetMapping("/{userId}")
    public ResponseEntity<List<OrderDTO>> getUserOrders(@PathVariable Long userId) {
        try {
            // Lấy danh sách tất cả đơn hàng của người dùng
            List<OrderDTO> orders = orderService.getOrdersByUserId(userId);
            return ResponseEntity.ok(orders);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // API để lấy chi tiết của một đơn hàng cụ thể
    @GetMapping("/details/{orderId}")
    public ResponseEntity<OrderDTO> getOrderDetails(@PathVariable Long orderId) {
        try {
            // Lấy chi tiết đơn hàng theo orderId
            OrderDTO order = orderService.getOrderById(orderId);
            return ResponseEntity.ok(order);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // API để hủy đơn hàng
    @DeleteMapping("/cancel/{orderId}")
    public ResponseEntity<String> cancelOrder(@PathVariable Long orderId) {
        try {
            // Gọi service để hủy đơn hàng
            orderService.cancelOrder(orderId);
            return ResponseEntity.ok("Order canceled successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Failed to cancel order.");
        }
    }
}
