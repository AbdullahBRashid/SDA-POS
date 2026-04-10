package com.DullPointers.model;

import com.DullPointers.model.enums.LogType;

import java.time.ZonedDateTime;

public class Log implements ILog {
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

    @Override
    public LogType getType() {
        return type;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public ZonedDateTime getTime() {
        return time;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setType(LogType type) {
        this.type = type;
    }
    @Override
    public void setDescription(String description) {
        this.description = description;
    }
    @Override
    public void setTime(ZonedDateTime time) {
        this.time = time;
    }
    @Override
    public void setId(Long id) {
        this.id = id;
    }
}
