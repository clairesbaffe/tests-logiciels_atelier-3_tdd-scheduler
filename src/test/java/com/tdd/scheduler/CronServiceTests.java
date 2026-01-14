package com.tdd.scheduler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CronServiceTests {

    private CronService scheduler;

    @BeforeEach
    void setup(){
        scheduler = new CronService();
    }

    @Test
    void initScheduler(){

    }

    @Test
    void saveNewScheduledTask(){
        Task task = new Task("Task 1", "0 0 13 * 5", () -> System.out.println("hello"));

        scheduler.setTask(task);

        ArrayList<Task> expected = new ArrayList<>();
        expected.add(task);

        assertEquals(expected, scheduler.getScheduledTasks());
    }

}
