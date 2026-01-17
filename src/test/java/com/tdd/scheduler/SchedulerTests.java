package com.tdd.scheduler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SchedulerTests {

    private Scheduler scheduler;

    @BeforeEach
    void setup(){
        scheduler = new Scheduler();
    }

    @Test
    void initScheduler(){
        scheduler = new Scheduler();

        assertEquals(scheduler.getClock(), Clock.systemDefaultZone());
    }

    @Test
    void initSchedulerWithClock(){
        MutableClock clock = new MutableClock(Instant.parse("2025-01-17T12:00:00Z"));

        scheduler = new Scheduler(clock);

        assertEquals(scheduler.getClock(), clock);
    }

    @Test
    void saveNewScheduledTask(){
        Task task = new Task("Task 1", "0 0 0 13 * 5", () -> System.out.println("hello"));

        scheduler.setTask(task);

        ArrayList<Task> expected = new ArrayList<>();
        expected.add(task);

        assertEquals(expected, scheduler.getScheduledTasks());
    }

    @Test
    void scheduledTaskExecutesAction(){
        MutableClock clock = new MutableClock(Instant.parse("2025-01-17T12:00:00Z"));
        scheduler = new Scheduler(clock);

        Runnable actionMock = mock(Runnable.class);
        Task task = new Task("Task 1", "0 1 * * * *", actionMock);
        scheduler.setTask(task);

        clock.plusSeconds(30);
        scheduler.update();

        verify(actionMock, never()).run();

        clock.plusSeconds(30);
        scheduler.update();

        verify(actionMock, times(1)).run();
    }

    static class MutableClock extends Clock {
        private Instant instant;
        private final ZoneId zone = ZoneId.of("UTC");

        public MutableClock(Instant start) { this.instant = start; }

        public void plusSeconds(long seconds) {
            this.instant = this.instant.plus(seconds, ChronoUnit.SECONDS);
        }
        public void plusMinutes(long minutes) {
            this.instant = this.instant.plus(minutes, ChronoUnit.MINUTES);
        }
        public void plusHours(long hours) {
            this.instant = this.instant.plus(hours, ChronoUnit.HOURS);
        }

        @Override public ZoneId getZone() { return zone; }
        @Override public Clock withZone(ZoneId zone) { return this; }
        @Override public Instant instant() { return instant; }
    }

}
