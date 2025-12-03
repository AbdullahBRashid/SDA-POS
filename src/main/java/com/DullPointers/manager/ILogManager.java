package com.DullPointers.manager;

import com.DullPointers.model.enums.LogType;

public interface ILogManager {
    void saveLog(LogType logType, String description);
}
