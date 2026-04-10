package com.DullPointers.manager;

import com.DullPointers.model.IShift;
import com.DullPointers.model.IUser;
import com.DullPointers.model.Shift;
import com.DullPointers.repository.ShiftRepository;
import com.DullPointers.repository.UserRepository;
import com.DullPointers.util.PasswordUtil;

import java.time.ZonedDateTime;
import java.util.Optional;

public class AuthManager implements IAuthManager {
    // Singleton
    private final UserRepository userRepository;
    private final ShiftRepository shiftRepository;
    private IUser currentUser;
    private IShift currentShift;

    public AuthManager(UserRepository userRepository, ShiftRepository shiftRepository) {
        this.userRepository = userRepository;
        this.shiftRepository = shiftRepository;
    }

    // Logic for Req 14: Authentication
    @Override
    public IUser authenticate(String username, String pin) throws SecurityException {
        Optional<IUser> userOpt = userRepository.findByUsername(username);

        if (userOpt.isPresent()) {
            IUser user = userOpt.get();
            // In real life, use BCrypt here, not equals()
            String storedHash = user.getPasswordHash();

            if (PasswordUtil.check(pin, storedHash)) {
                this.currentUser = user;
                ZonedDateTime now = ZonedDateTime.now();
                this.currentShift = new Shift(user.getUsername(), now);
                // Req 15: Log activity
                System.out.println("LOG: User " + username + " logged in at " +  now);
                return user;
            }
        }
        throw new SecurityException("Invalid credentials");
    }

    @Override
    public void logout() {
        if (currentUser != null && currentShift != null) {
            ZonedDateTime now = ZonedDateTime.now();
            System.out.println("LOG: User " + currentUser.getUsername() + " logged out at " + now);
            currentShift.setEndTime(ZonedDateTime.now());
            shiftRepository.save(currentShift);
        }
        this.currentUser = null;
        this.currentShift = null;
    }

    @Override
    public IUser getCurrentUser() {
        if (currentUser == null) {
            throw new SecurityException("No user logged in.");
        }
        return currentUser;
    }
}