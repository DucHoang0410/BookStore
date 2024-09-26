package com.bookstore.service;

import com.bookstore.dto.OrderDTO;
import com.bookstore.dto.OrderItemDTO;
import com.bookstore.entity.*;
import com.bookstore.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    // Lấy danh sách tất cả các đơn hàng của một người dùng và trả về dưới dạng DTO
    public List<OrderDTO> getOrdersByUserId(Long userId) {
        // Kiểm tra sự tồn tại của người dùng
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        // Lấy danh sách đơn hàng của người dùng
        List<Order> orders = orderRepository.findByUser(user);

        // Chuyển đổi từ Order sang OrderDTO
        return orders.stream().map(order -> {
            // Chuyển đổi từng OrderItem sang OrderItemDTO
            List<OrderItemDTO> itemDTOs = order.getItems().stream().map(item -> {
                return new OrderItemDTO(
                        item.getBook().getId(),
                        item.getBook().getTitle(),
                        item.getQuantity(),
                        item.getPrice()
                );
            }).collect(Collectors.toList());

            // Chuyển đổi Order thành OrderDTO
            return new OrderDTO(
                    order.getId(),
                    order.getOrderDate(),
                    order.getTotal(),
                    order.getStatus(),
                    itemDTOs
            );
        }).collect(Collectors.toList());
    }
}
