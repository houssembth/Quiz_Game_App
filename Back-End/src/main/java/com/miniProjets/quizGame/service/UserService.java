package com.miniProjets.quizGame.service;

import com.miniProjets.quizGame.model.User;
import com.miniProjets.quizGame.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User updateUser(Long id, User userDetails) {
        User user = getUserById(id);
        user.setUsername(userDetails.getUsername());
        user.setEmail(userDetails.getEmail());
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void deleteUser(Long id) {
        User user = getUserById(id);
        userRepository.delete(user);
    }

    public User updateUserRole(Long id, String role) {
        // Validate role
        if (!role.equals("PLAYER") && !role.equals("ADMIN")) {
            throw new RuntimeException("Invalid role. Must be PLAYER or ADMIN");
        }

        User user = getUserById(id);
        user.setRole(role);
        return userRepository.save(user);
    }
}