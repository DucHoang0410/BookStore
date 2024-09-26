package com.bookstore.service;

import com.bookstore.dto.CartDTO;
import com.bookstore.dto.CartItemDTO;
import com.bookstore.entity.*;
import com.bookstore.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    // Thêm sách vào giỏ hàng
    public CartDTO addToCart(Long userId, Long bookId, int quantity) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + bookId));

        Cart cart = cartRepository.findByUser(user).orElse(null);

        // Kiểm tra nếu không có giỏ hàng, tạo mới giỏ hàng
        if (cart == null) {
            cart = new Cart();
            cart.setUser(user);
            cart = cartRepository.save(cart);
        }

        Optional<CartItem> existingCartItem = cart.getItems().stream()
                .filter(item -> item.getBook().getId().equals(bookId))
                .findFirst();

        if (existingCartItem.isPresent()) {
            CartItem cartItem = existingCartItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
            cartItem.setPrice(book.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
            cartItemRepository.save(cartItem);
        } else {
            CartItem cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setBook(book);
            cartItem.setQuantity(quantity);
            cartItem.setPrice(book.getPrice().multiply(BigDecimal.valueOf(quantity)));
            cart.getItems().add(cartItem);
            cartItemRepository.save(cartItem);
        }

        updateCartTotal(cart);

        return mapToCartDTO(cart);
    }

    // Xóa sách khỏi giỏ hàng
    public CartDTO removeFromCart(Long userId, Long bookId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found for user: " + userId));

        cart.getItems().removeIf(cartItem -> cartItem.getBook().getId().equals(bookId));

        updateCartTotal(cart);

        return mapToCartDTO(cart);
    }

    // Cập nhật số lượng sách trong giỏ hàng
    public CartDTO updateCartItemQuantity(Long userId, Long bookId, int quantity) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found for user: " + userId));

        CartItem cartItem = cart.getItems().stream()
                .filter(item -> item.getBook().getId().equals(bookId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Book not found in cart"));

        cartItem.setQuantity(quantity);
        cartItem.setPrice(cartItem.getBook().getPrice().multiply(BigDecimal.valueOf(quantity)));

        cartItemRepository.save(cartItem);

        updateCartTotal(cart);

        return mapToCartDTO(cart);
    }

    // Lấy giỏ hàng của người dùng
    public CartDTO getCartByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found for user: " + userId));

        return mapToCartDTO(cart);
    }

    private void updateCartTotal(Cart cart) {
        BigDecimal total = cart.getItems().stream()
                .map(CartItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        cart.setTotal(total);
        cartRepository.save(cart);
    }

    // Cập nhật phương thức để bao gồm cả imageUrl
    private CartDTO mapToCartDTO(Cart cart) {
        List<CartItemDTO> cartItems = cart.getItems().stream()
                .map(item -> new CartItemDTO(
                        item.getId(),
                        item.getBook().getId(),
                        item.getBook().getTitle(),
                        item.getQuantity(),
                        item.getPrice(),
                        item.getBook().getImageUrl() // Thêm imageUrl vào đây
                ))
                .collect(Collectors.toList());

        return new CartDTO(cart.getId(), cart.getUser().getId(), cart.getTotal(), cartItems);
    }
}
