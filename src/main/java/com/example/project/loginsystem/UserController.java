package com.example.project.loginsystem;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserRepository userRepository;
    
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user, HttpServletResponse response) {
        Optional<User> found = userRepository.findByUsernameAndPassword(user.getUsername(), user.getPassword());

        if (found.isPresent()) {
            // Set a cookie on successful login
            Cookie authCookie = new Cookie("auth", "true");
            authCookie.setHttpOnly(false); // Set true in production
            authCookie.setPath("/");
            authCookie.setMaxAge(60 * 60); // 1 hour
            response.addCookie(authCookie);

            return ResponseEntity.ok().body(new Message("Login successful!"));
        } else {
            return ResponseEntity.status(401).body(new Message("Invalid username or password!"));
        }
    }

    public static class Message {
        private String message;

        public Message(String message) { this.message = message; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }
}
