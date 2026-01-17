package com.tdd.scheduler;

import lombok.Getter;

import java.time.Clock;
import java.util.ArrayList;

@Getter
public class Scheduler {

    private final Clock clock;
    private final ArrayList<Task> scheduledTasks = new ArrayList<>();

    public Scheduler(){
        this.clock = Clock.systemDefaultZone();
    }

    public Scheduler(Clock clock){
        this.clock = clock;
    }


    void setTask(Task task){
        scheduledTasks.add(task);
    }

    ArrayList<Task> getScheduledTasks(){
        return scheduledTasks;
    }

    void update(){

    }
}
