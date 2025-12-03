package com.DullPointers.repository;

import com.DullPointers.model.IUser;
import java.util.Optional;
import java.util.List;

public interface UserRepository {
    Optional<IUser> findByUsername(String username);
    List<IUser> findAll();
    void save(IUser user);
    void delete(IUser user);
}