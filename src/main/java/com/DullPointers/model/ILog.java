package com.DullPointers.model;

import com.DullPointers.model.enums.LogType;

import java.time.ZonedDateTime;

public interface ILog {
    LogType getType();

    String getDescription();

    ZonedDateTime getTime();

    Long getId();

    void setType(LogType type);

    void setDescription(String description);

    void setTime(ZonedDateTime time);

    void setId(Long id);
}
