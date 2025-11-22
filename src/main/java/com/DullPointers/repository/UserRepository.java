package com.DullPointers.repository;

import com.DullPointers.model.User;
import java.util.Optional;
import java.util.List;

public interface UserRepository {
    Optional<User> findByUsername(String username);

    List<User> findAll();
    void save(User user);
    void delete(User user);
}