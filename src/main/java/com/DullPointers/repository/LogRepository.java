package com.DullPointers.repository;

import com.DullPointers.model.ILog;
import com.DullPointers.model.enums.LogType;

import java.util.List;

public interface LogRepository {
    List<ILog> findAll();
    List<ILog> findByType(LogType type);
    void save(ILog log);
}
