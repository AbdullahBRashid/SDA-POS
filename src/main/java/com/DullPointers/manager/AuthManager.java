package com.DullPointers.manager;

import com.DullPointers.model.User;
import com.DullPointers.repository.UserRepository;
import com.DullPointers.util.PasswordUtil;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Optional;

public class AuthManager {
    private final UserRepository userRepository;
    private User currentUser;

    public AuthManager(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Logic for Req 14: Authentication
    public User authenticate(String username, String pin) throws SecurityException {
        Optional<User> userOpt = userRepository.findByUsername(username);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            // In real life, use BCrypt here, not equals()
            String storedHash = user.getPasswordHash();

            if (PasswordUtil.check(pin, storedHash)) {
                this.currentUser = user;
                // Req 15: Log activity
                System.out.println("LOG: User " + username + " logged in.");
                return user;
            }
        }
        throw new SecurityException("Invalid credentials");
    }

    public void logout() {
        if (currentUser != null) {
            System.out.println("LOG: User " + currentUser.getUsername() + " logged out.");
        }
        this.currentUser = null;
    }

    public User getCurrentUser() {
        if (currentUser == null) {
            throw new SecurityException("No user logged in.");
        }
        return currentUser;
    }
}