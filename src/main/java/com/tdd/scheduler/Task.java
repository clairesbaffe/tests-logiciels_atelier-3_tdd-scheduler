package com.tdd.scheduler;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Task {

    private String name;
    private String period;
    private Runnable action;

    public Task(String name, String period, Runnable action){
        this.name = name;
        this.period = period;
        this.action = action;
    }
}
