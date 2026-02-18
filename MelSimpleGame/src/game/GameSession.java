package game;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.Timer;

public class GameSession {
    // for global score/stats
    private static int totalLifetimeScore = 0; // to be added to a report
    private static long totalLifetimeSeconds = 0; // to be added to a report
    private static Map<LocalDate, DailyStats> history = new HashMap<>();

    // for current session only
    private int sessionScore;
    private int secondsRemaining;
    private Timer countdownTimer;
    private Runnable onTimeUp;
    private boolean isFinished = false; // need to add a check for this later
    private int streak = 0;

    // EFFECTS: initializes a new game session with the given duration and callback
    // for when time is up
    public GameSession(int duration, Runnable onTimeUp, JComponent owner) {
        this.sessionScore = 0;
        this.secondsRemaining = duration;
        this.streak = 0;
        this.onTimeUp = onTimeUp;
        this.countdownTimer = new Timer(1000, e -> {
            tick();
            owner.repaint();
        });
        this.countdownTimer.start();
    }

    // EFFECTS: handles the countdown logic for each tick of the timer
    private void tick() {
        if (secondsRemaining > 0) {
            secondsRemaining--;
            totalLifetimeSeconds++;
        } else {
            endSession();
        }
    }

    // EFFECTS: calculates and adds points based on the number of attempts and
    // current streak
    public void addPointsLogic(int attemptsThisRound, int multiplier) {
        int points = Math.max(-10, (10 * multiplier) - (attemptsThisRound * 5));
        if (attemptsThisRound == 0)
            streak++;
        else
            streak = 0;
        points = streakBonus(points, multiplier);
        addPoints(points);
    }

    // EFFECTS: adds points to the current session score and updates lifetime stats
    public void addPoints(int points) {
        sessionScore += points;
        totalLifetimeScore += points;
        updateDailyStats(points);
    }

    // EFFECTS: calculates bonus points based on the current streak
    public int streakBonus(int points, int multiplier) {
        if (streak > 5)
            points += 5;
        if (streak > 10)
            points += 10;
        return points;
    }

    // EFFECTS: ends the current game session, stops the timer, and triggers the
    // time-up callback
    private void endSession() {
        isFinished = true;
        countdownTimer.stop();
        if (onTimeUp != null)
            onTimeUp.run();
    }

    // EFFECTS: updates the daily stats for the current date with the points scored
    // in this session
    private void updateDailyStats(int points) {
        LocalDate today = LocalDate.now();
        history.putIfAbsent(today, new DailyStats());
        history.get(today).addScore(points);
    }

    // Inner class to track daily statistics
    public static class DailyStats {
        int dailyScore = 0;

        void addScore(int p) {
            dailyScore += p;
        }

        public int getDailyScore() {
            return dailyScore;
        }
    }

    // Getters and setters
    public static DailyStats getStatsForDate(LocalDate date) {
        return history.getOrDefault(date, new DailyStats());
    }

    // EFFECTS: updates the duration of the current session, with a min of 1 minute
    public void updateDuration(int newDuration) {
        if (newDuration <= 0)
            secondsRemaining = 60;
        else
            this.secondsRemaining = newDuration;
    }

    public String getTimeString() {
        return String.format("%02d:%02d", secondsRemaining / 60, secondsRemaining % 60);
    }

    public int getSessionScore() {
        return sessionScore;
    }
}
