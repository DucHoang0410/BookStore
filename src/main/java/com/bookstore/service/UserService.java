package com.bookstore.service;

import com.bookstore.dto.UserDTO;
import com.bookstore.entity.User;
import com.bookstore.repository.UserRepository;
import com.bookstore.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    // Đăng ký người dùng
    public String registerUser(String username, String password) {
        if (userRepository.existsByUsername(username)) {
            return "Username đã tồn tại!";
        }
        User user = new User(username, passwordEncoder.encode(password), null);
        userRepository.save(user);
        return "Đăng ký thành công!";
    }

    // Đăng nhập người dùng
    public UserDTO loginUser(String username, String password) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (passwordEncoder.matches(password, user.getPassword())) {
                String token = jwtUtils.generateJwtToken(user.getUsername());
                String refreshToken = jwtUtils.generateJwtToken(user.getUsername());
                user.setRefreshToken(refreshToken);
                userRepository.save(user);
                return new UserDTO(user.getId(), user.getUsername(), token, refreshToken);
            }
        }
        return null;
    }

    // Làm mới token
    public UserDTO refreshToken(String refreshToken) {
        if (jwtUtils.validateJwtToken(refreshToken)) {
            String username = jwtUtils.getUsernameFromJwtToken(refreshToken);
            Optional<User> userOptional = userRepository.findByUsername(username);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                String newToken = jwtUtils.generateJwtToken(user.getUsername());
                return new UserDTO(user.getId(), user.getUsername(), newToken, refreshToken);
            }
        }
        return null;
    }
}
