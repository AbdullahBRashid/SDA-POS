package com.DullPointers.repository.impl;

import com.DullPointers.model.Log;
import com.DullPointers.model.Sale;
import com.DullPointers.model.enums.LogType;
import com.DullPointers.repository.LogRepository;
import com.DullPointers.util.JsonDataStore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FileLogRepository implements LogRepository {
    private static final String FILE_PATH = "logs.json";

    private final List<Log> database;

    public FileLogRepository() {
        this.database = JsonDataStore.load(FILE_PATH, Log[].class);
    }

    @Override
    public List<Log> findByType(LogType type) {
        return database.stream()
                .filter(l -> l.getType().equals(type))
                .toList();
    }

    @Override
    public List<Log> findAll() {
        return new ArrayList<>(database);
    }

    @Override
    public void save(Log log) {
        if (log.getId() == null) {
            long maxId = database.stream().mapToLong(Log::getId).max().orElse(0);
            log.setId(maxId + 1);
        }

        database.removeIf(l -> Objects.equals(l.getId(), log.getId()));
        database.add(log);

        JsonDataStore.save(database, FILE_PATH);
    }
}
