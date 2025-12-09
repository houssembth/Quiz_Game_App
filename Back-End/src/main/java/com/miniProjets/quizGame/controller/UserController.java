package com.miniProjets.quizGame.controller;

import com.miniProjets.quizGame.model.User;
import com.miniProjets.quizGame.security.model.UpdateUserRoleRequest;
import com.miniProjets.quizGame.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('PLAYER', 'ADMIN')")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        try {
            User user = userService.getUserById(id);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('PLAYER', 'ADMIN')")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User user) {
        try {
            User updatedUser = userService.updateUser(id, user);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok("User deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateUserRole(
            @PathVariable Long id,
            @RequestBody UpdateUserRoleRequest request,
            Authentication authentication) {
        try {
            // Get current authenticated user's email
            String currentUserEmail = authentication.getName();

            // Get the target user
            User targetUser = userService.getUserById(id);

            // Prevent admin from changing their own role
            if (targetUser.getEmail().equals(currentUserEmail)) {
                return ResponseEntity.badRequest().body("You cannot change your own role");
            }

            User updatedUser = userService.updateUserRole(id, request.getRole());
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}