package com.DullPointers.manager;

import com.DullPointers.model.Log;
import com.DullPointers.model.enums.LogType;
import com.DullPointers.repository.LogRepository;

public class LogManager {
    private final LogRepository logRepo;

    public LogManager(LogRepository logRepo) {
        this.logRepo = logRepo;
    }

    public void saveLog(LogType logType, String description) {
        logRepo.save(new Log(logType, description));
    }

}
