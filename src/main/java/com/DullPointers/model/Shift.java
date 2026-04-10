package com.DullPointers.model;

import java.time.ZonedDateTime;

public class Shift implements IShift {
    private String username;
    private ZonedDateTime startTime;
    private ZonedDateTime endTime;

    public Shift() {}

    public Shift(String username, ZonedDateTime startTime) {
        this.username = username;
        this.startTime = startTime;
    }

    @Override
    public void setEndTime(ZonedDateTime endTime) {
        this.endTime = endTime;
    }
}
