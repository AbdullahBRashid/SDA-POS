package com.DullPointers.repository.impl;

import com.DullPointers.model.Shift;
import com.DullPointers.repository.ShiftRepository;
import com.DullPointers.util.JsonDataStore;

import java.util.List;

public class FileShiftRepository implements ShiftRepository {
    private static final String FILE_PATH = "shifts.json";
    private List<Shift> database;

    public FileShiftRepository() {
        this.database = JsonDataStore.load(FILE_PATH, Shift[].class);
    }

    @Override
    public void save(Shift shift) {
        database.add(shift);
        JsonDataStore.save(database, FILE_PATH);
    }
}
