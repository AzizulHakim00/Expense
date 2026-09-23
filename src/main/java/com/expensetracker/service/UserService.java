package com.expensetracker.service;

import com.expensetracker.dto.RegisterRequest;
import com.expensetracker.model.Role;
import com.expensetracker.model.User;
import com.expensetracker.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Locale;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(RegisterRequest request) {
        String email = request.getEmail().trim().toLowerCase(Locale.ROOT);

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }
        if (userRepository.existsByEmailIgnoreCase(email)) {
            throw new IllegalArgumentException("An account already exists with this email");
        }

        User user = new User();
        user.setFullName(request.getFullName().trim());
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);
        user.setEnabled(true);
        user.setCreatedAt(Instant.now());
        return userRepository.save(user);
    }

    public User currentUser(Authentication authentication) {
        if (authentication == null) {
            throw new IllegalStateException("No authenticated user");
        }
        return userRepository.findByEmailIgnoreCase(authentication.getName())
                .orElseThrow(() -> new IllegalStateException("Authenticated user was not found"));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void toggleEnabled(String userId, String currentAdminEmail) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (user.getEmail().equalsIgnoreCase(currentAdminEmail)) {
            throw new IllegalArgumentException("You cannot disable your own admin account");
        }
        if (user.getRole() == Role.ADMIN) {
            throw new IllegalArgumentException("Admin accounts cannot be disabled here");
        }

        user.setEnabled(!user.isEnabled());
        userRepository.save(user);
    }
}
