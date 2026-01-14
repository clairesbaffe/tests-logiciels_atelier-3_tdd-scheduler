package com.tdd.scheduler;

import java.util.ArrayList;

public class CronService {

    private ArrayList<Task> scheduledTasks = new ArrayList<>();

    public CronService(){

    }

    void setTask(Task task){
        scheduledTasks.add(task);
    }

    ArrayList<Task> getScheduledTasks(){
        return scheduledTasks;
    }
}
