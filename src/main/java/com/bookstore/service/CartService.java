package com.bookstore.service;

import com.bookstore.dto.CartDTO;
import com.bookstore.dto.CartItemDTO;
import com.bookstore.entity.Book;
import com.bookstore.entity.Cart;
import com.bookstore.entity.CartItem;
import com.bookstore.entity.User;
import com.bookstore.repository.BookRepository;
import com.bookstore.repository.CartItemRepository;
import com.bookstore.repository.CartRepository;
import com.bookstore.repository.UserRepository;
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

    // Thêm sách vào giỏ hàng
    public CartDTO addToCart(Long userId, Long bookId, int quantity) {
        // Tìm người dùng
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        // Tìm sách
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + bookId));

        // Tìm hoặc tạo mới giỏ hàng của người dùng
        Cart cart = cartRepository.findByUser(user).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUser(user);
            return cartRepository.save(newCart);
        });

        // Tìm hoặc thêm mới mục trong giỏ hàng
        Optional<CartItem> existingCartItem = cart.getItems().stream()
                .filter(item -> item.getBook().getId().equals(bookId))
                .findFirst();

        if (existingCartItem.isPresent()) {
            // Nếu sách đã có trong giỏ hàng, cập nhật số lượng và giá
            CartItem cartItem = existingCartItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
            cartItem.setPrice(book.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
            cartItemRepository.save(cartItem);
        } else {
            // Nếu sách chưa có trong giỏ hàng, thêm sách mới
            CartItem cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setBook(book);
            cartItem.setQuantity(quantity);
            cartItem.setPrice(book.getPrice().multiply(BigDecimal.valueOf(quantity)));
            cart.getItems().add(cartItem);  // Thêm vào danh sách items của giỏ hàng
            cartItemRepository.save(cartItem);
        }

        // Tính lại tổng giá trị của giỏ hàng
        updateCartTotal(cart);

        // Trả về DTO của giỏ hàng đã cập nhật
        return mapToCartDTO(cart);
    }

    // Xóa sách khỏi giỏ hàng
    public CartDTO removeFromCart(Long userId, Long bookId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found for user: " + userId));

        // Xóa sách khỏi giỏ hàng
        cart.getItems().removeIf(cartItem -> cartItem.getBook().getId().equals(bookId));

        // Tính toán lại tổng giá trị sau khi xóa
        updateCartTotal(cart);

        return mapToCartDTO(cart);
    }

    // Cập nhật số lượng sách trong giỏ hàng
    public CartDTO updateCartItemQuantity(Long userId, Long bookId, int quantity) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found for user: " + userId));

        // Tìm mục giỏ hàng cần cập nhật
        CartItem cartItem = cart.getItems().stream()
                .filter(item -> item.getBook().getId().equals(bookId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Book not found in cart"));

        // Cập nhật số lượng và giá
        cartItem.setQuantity(quantity);
        cartItem.setPrice(cartItem.getBook().getPrice().multiply(BigDecimal.valueOf(quantity)));

        // Lưu thay đổi
        cartItemRepository.save(cartItem);

        // Tính toán lại tổng giỏ hàng
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

    // Phương thức tính tổng giá giỏ hàng
    private void updateCartTotal(Cart cart) {
        BigDecimal total = cart.getItems().stream()
                .map(CartItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        cart.setTotal(total);
        cartRepository.save(cart);
    }

    // Chuyển đổi từ Cart entity sang CartDTO
    private CartDTO mapToCartDTO(Cart cart) {
        List<CartItemDTO> cartItems = cart.getItems().stream()
                .map(item -> new CartItemDTO(
                        item.getId(),
                        item.getBook().getId(),
                        item.getBook().getTitle(),
                        item.getQuantity(),
                        item.getPrice()))
                .collect(Collectors.toList());

        return new CartDTO(cart.getId(), cart.getUser().getId(), cart.getTotal(), cartItems);
    }
}
