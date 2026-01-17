package com.tdd.scheduler;

import lombok.Getter;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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

    void setTask(String name, Task task){

    }

    ArrayList<Task> getScheduledTasks(){
        return scheduledTasks;
    }


    void update() {
        LocalDateTime now = LocalDateTime.now(clock).truncatedTo(ChronoUnit.SECONDS);

        scheduledTasks.forEach(task -> {
            LocalDateTime nextExecution = task.getCronExpression().next(now.minusSeconds(1));

            if (nextExecution != null && nextExecution.isEqual(now))
                task.getAction().run();
        });
    }
}
