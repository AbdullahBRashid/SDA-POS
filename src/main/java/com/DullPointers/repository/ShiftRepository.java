package com.DullPointers.repository;

import com.DullPointers.model.Shift;

import java.util.List;
import java.util.Optional;

public interface ShiftRepository {
    void save(Shift shift);
}