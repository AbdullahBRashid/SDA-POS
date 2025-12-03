package com.DullPointers.repository;

import com.DullPointers.model.Log;
import com.DullPointers.model.enums.LogType;

import java.util.List;

public interface LogRepository {
    List<Log> findAll();
    List<Log> findByType(LogType type);
    void save(Log log);
}
