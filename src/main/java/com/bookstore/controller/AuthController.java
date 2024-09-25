package com.bookstore.controller;

import com.bookstore.dto.UserDTO;
import com.bookstore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestParam String username, @RequestParam String password) {
        String response = userService.registerUser(username, password);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestParam String username, @RequestParam String password) {
        UserDTO userDTO = userService.loginUser(username, password);
        if (userDTO == null) {
            return ResponseEntity.badRequest().body("Đăng nhập thất bại");
        }
        return ResponseEntity.ok(userDTO);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestParam String refreshToken) {
        UserDTO userDTO = userService.refreshToken(refreshToken);
        if (userDTO == null) {
            return ResponseEntity.badRequest().body("Token không hợp lệ");
        }
        return ResponseEntity.ok(userDTO);
    }
}
