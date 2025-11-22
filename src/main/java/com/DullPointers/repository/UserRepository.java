package com.DullPointers.repository;

import com.DullPointers.model.User;
import java.util.Optional;

public interface UserRepository {
    Optional<User> findByUsername(String username);
}