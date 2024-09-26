package com.bookstore.service;

import com.bookstore.dto.OrderBookDTO;
import com.bookstore.dto.OrderDTO;
import com.bookstore.dto.OrderItemDTO;
import com.bookstore.entity.Cart;
import com.bookstore.entity.Order;
import com.bookstore.entity.OrderItem;
import com.bookstore.entity.User;
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
    private OrderItemRepository orderItemRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private UserRepository userRepository;

    // Tạo đơn hàng từ giỏ hàng
    public List<OrderBookDTO> createOrderFromCart(Long userId) {
        // Kiểm tra sự tồn tại của người dùng
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        // Lấy giỏ hàng của người dùng
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found for user: " + userId));

        // Tạo đơn hàng mới
        Order order = new Order();
        order.setUser(user);
        order.setTotal(cart.getTotal());
        order.setStatus("PAID"); // Trạng thái đơn hàng là đã thanh toán
        order.setOrderDate(java.time.LocalDateTime.now());

        // Lưu đơn hàng vào cơ sở dữ liệu
        orderRepository.save(order);

        // Tạo các mục trong đơn hàng từ các sản phẩm trong giỏ hàng
        List<OrderItem> orderItems = cart.getItems().stream().map(cartItem -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setBook(cartItem.getBook());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getPrice());
            return orderItem;
        }).collect(Collectors.toList());

        // Lưu các mục đơn hàng
        orderItemRepository.saveAll(orderItems);

        // Xóa giỏ hàng sau khi chuyển thành đơn hàng
        cartItemRepository.deleteAll(cart.getItems());
        cartRepository.delete(cart);

        // **Tạo giỏ hàng mới cho người dùng**
        Cart newCart = new Cart();
        newCart.setUser(user);
        cartRepository.save(newCart);

        // Trả về danh sách các sản phẩm đã mua trong đơn hàng dưới dạng DTO
        return orderItems.stream()
                .map(orderItem -> new OrderBookDTO(
                        orderItem.getBook().getId(),
                        orderItem.getBook().getTitle(),
                        orderItem.getPrice().doubleValue(),
                        orderItem.getQuantity(),
                        order.getOrderDate()))
                .collect(Collectors.toList());
    }

    // Lấy danh sách tất cả các đơn hàng của một người dùng
    public List<OrderDTO> getOrdersByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        List<Order> orders = orderRepository.findByUser(user);

        return orders.stream().map(order -> {
            List<OrderItemDTO> itemDTOs = order.getItems().stream().map(item -> {
                return new OrderItemDTO(
                        item.getBook().getId(),
                        item.getBook().getTitle(),
                        item.getQuantity(),
                        item.getPrice(),
                        item.getBook().getImageUrl() // Thêm imageUrl
                );
            }).collect(Collectors.toList());

            return new OrderDTO(
                    order.getId(),
                    order.getOrderDate(),
                    order.getTotal(),
                    order.getStatus(),
                    itemDTOs
            );
        }).collect(Collectors.toList());
    }

    // Lấy chi tiết của một đơn hàng cụ thể
    public OrderDTO getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));

        List<OrderItemDTO> itemDTOs = order.getItems().stream().map(item -> {
            return new OrderItemDTO(
                    item.getBook().getId(),
                    item.getBook().getTitle(),
                    item.getQuantity(),
                    item.getPrice(),
                    item.getBook().getImageUrl() // Thêm imageUrl
            );
        }).collect(Collectors.toList());

        return new OrderDTO(order.getId(), order.getOrderDate(), order.getTotal(), order.getStatus(), itemDTOs);
    }

    // Hủy đơn hàng
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));

        order.setStatus("CANCELED");
        orderRepository.save(order);
    }
}
