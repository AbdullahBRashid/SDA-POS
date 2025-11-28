package com.DullPointers.model;

import java.time.ZonedDateTime;

public class Shift {
    private int id;
    private String username;
    private static int curr_id = 0;

    private ZonedDateTime startTime;
    private ZonedDateTime endTime;

    public Shift(String username, ZonedDateTime startTime) {
        this.id = ++curr_id;
        this.username = username;
        this.startTime = startTime;
    }

    public void setEndTime(ZonedDateTime endTime) {
        this.endTime = endTime;
    }
}
