package com.DullPointers.model;

import com.DullPointers.model.enums.LogType;

import java.time.ZonedDateTime;

public class Log {
    Long id;
    LogType type;
    String description;
    ZonedDateTime time;

    public Log() {};

    public Log(LogType type, String description) {
        this.type = type;
        this.description = description;
        this.time = ZonedDateTime.now();
    }

    public LogType getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public ZonedDateTime getTime() {
        return time;
    }

    public Long getId() {
        return id;
    }

    public void setType(LogType type) {
        this.type = type;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setTime(ZonedDateTime time) {
        this.time = time;
    }
    public void setId(Long id) {
        this.id = id;
    }
}
