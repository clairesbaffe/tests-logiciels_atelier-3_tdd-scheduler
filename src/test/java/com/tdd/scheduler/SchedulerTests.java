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
        MutableClock clock = new MutableClock(Instant.parse("2026-01-17T12:00:00Z"));

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
    void updateScheduledTask(){
        Task task = new Task("Task 1", "0 0 0 13 * 5", () -> System.out.println("hello"));
        scheduler.setTask(task);

        Task modifiedTask = new Task("Task 2", "0 0 0 13 * 5", () -> System.out.println("hello"));
        scheduler.setTask("Task 1", modifiedTask);

        ArrayList<Task> expected = new ArrayList<>();
        expected.add(modifiedTask);

        assertEquals(expected, scheduler.getScheduledTasks());
    }

    @Test
    void notAddingScheduledTaskIfNotExistingWhenUpdating(){
        Task modifiedTask = new Task("Task 2", "0 0 0 13 * 5", () -> System.out.println("hello"));
        scheduler.setTask("Task 1", modifiedTask);

        ArrayList<Task> expected = new ArrayList<>();

        assertEquals(expected, scheduler.getScheduledTasks());
    }

    @Test
    void scheduledTaskExecutesAction(){
        MutableClock clock = new MutableClock(Instant.parse("2026-01-17T12:00:00Z"));
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

    @Test
    void executeTaskEverySecond(){
        MutableClock clock = new MutableClock(Instant.parse("2026-01-17T12:00:00Z"));
        scheduler = new Scheduler(clock);

        Runnable actionMock = mock(Runnable.class);
        Task task = new Task("Task 1", "* * * * * *", actionMock);
        scheduler.setTask(task);

        for (int i = 0; i < 10; i++) {
            clock.plusSeconds(1);
            scheduler.update();
        }

        verify(actionMock, times(10)).run();
    }

    @Test
    void executeTaskEveryFiveMinutes(){
        MutableClock clock = new MutableClock(Instant.parse("2026-01-17T12:00:00Z"));
        scheduler = new Scheduler(clock);

        Runnable actionMock = mock(Runnable.class);
        Task task = new Task("Task 1", "0 */5 * * * *", actionMock);
        scheduler.setTask(task);

        for (int i = 0; i < 10; i++) {
            clock.plusMinutes(1);
            scheduler.update();
        }

        verify(actionMock, times(2)).run();
    }

    @Test
    void executeTaskEvery30MinutesOf8h9h10hOn1stAnd15th(){
        MutableClock clock = new MutableClock(Instant.parse("2025-02-01T00:00:00Z"));
        scheduler = new Scheduler(clock);

        Runnable actionMock = mock(Runnable.class);
        Task task = new Task("Task 1", "0 0,30 8-10 1,15 * *", actionMock);
        scheduler.setTask(task);

        clock.plusHours(7); // 2026/02/01 7h00
        scheduler.update();
        clock.plusMinutes(90); // 2026/02/01 8h30
        scheduler.update(); // should execute now
        clock.plusHours(1); // 2026/02/01 9h30
        scheduler.update(); // should execute now
        clock.plusDays(8); // 2026/02/09 9h30
        scheduler.update();
        clock.plusDays(6); // 2026/02/15 9h30
        scheduler.update(); // should execute now

        verify(actionMock, times(3)).run();
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
        public void plusDays(long days) {
            this.instant = this.instant.plus(days, ChronoUnit.DAYS);
        }

        @Override public ZoneId getZone() { return zone; }
        @Override public Clock withZone(ZoneId zone) { return this; }
        @Override public Instant instant() { return instant; }
    }

}
