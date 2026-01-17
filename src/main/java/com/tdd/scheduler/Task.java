package com.tdd.scheduler;

import lombok.Getter;
import lombok.Setter;
import org.springframework.scheduling.support.CronExpression;

@Getter
@Setter
public class Task {

    private String name;
    private String period;
    private Runnable action;

    private CronExpression cronExpression;

    public Task(String name, String period, Runnable action){
        this.name = name;
        this.period = period;
        this.action = action;

        this.cronExpression = CronExpression.parse(period);
    }
}
