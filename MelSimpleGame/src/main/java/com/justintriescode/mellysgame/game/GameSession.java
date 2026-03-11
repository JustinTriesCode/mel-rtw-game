package com.justintriescode.mellysgame.game;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.Timer;

public class GameSession {
    // for global score/stats
    private final PlayerProfile profile;
    private static Map<LocalDate, DailyStats> history = new HashMap<>();

    // for current session only
    private int sessionScore;
    private int secondsRemaining;
    private Timer countdownTimer;
    private Runnable onTimeUp;
    private boolean isFinished = false; // need to add a check for this later
    private int streak = 0;

    // for point calculation logic
    private static final int BASE_POINTS = 10;
    private static final int PENALTY_PER_ATTEMPT = 10;
    private static final int STREAK_THRESHOLD_MID = 10;
    private static final int STREAK_BONUS_MID = 16;

    // EFFECTS: initializes a new game session with the given duration and callback
    // for when time is up
    public GameSession(PlayerProfile profile, int duration, Runnable onTimeUp, JComponent owner) {
        this.profile = profile;
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
            profile.increaseLifetimeSeconds();
        } else {
            endSession();
        }
    }

    public void processScore(int attempts, double difficultyMultiplier, int speedBonus) {
        if (attempts == 0) {
            streak++;
        } else {
            streak = 0;
        }

        // base Calculation
        int basePoints = BASE_POINTS;
        int attemptPenalty = attempts * PENALTY_PER_ATTEMPT;

        // apply Multipliers and Bonuses
        double earnedPoints = (basePoints - attemptPenalty) * difficultyMultiplier;
        earnedPoints += speedBonus;
        earnedPoints += calculateStreakBonus();
        earnedPoints = Math.max(earnedPoints, -10 * difficultyMultiplier);
        int finalPoints = (int) Math.round(earnedPoints);
        applyPoints(finalPoints);
    }

    private int calculateStreakBonus() {
        if (streak > STREAK_THRESHOLD_MID)
            return STREAK_BONUS_MID;
        if (streak > STREAK_THRESHOLD_MID / 2)
            return STREAK_BONUS_MID / 2;
        return 0;
    }

    private void applyPoints(int points) {
        sessionScore += points;
        profile.increaseLifetimeScore(points);
        updateDailyStats(points);
    }

    // bonus points for specific milestones
    public void processMilestone(double difficultyMultiplier, int intensity) {
        int milestoneBase = 20;
        double totalBonus = (milestoneBase * intensity) * difficultyMultiplier;
        applyPoints((int) Math.round(totalBonus));
    }

    // EFFECTS: ends the current game session, stops the timer, and triggers the
    // time-up callback
    private void endSession() {
        isFinished = true;
        countdownTimer.stop();
        profile.updateStats(this.sessionScore);
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

    // EFFECTS: stops the countdown timer to prevent it from running in the
    // background
    public void pauseTimer() {
        if (countdownTimer != null) {
            countdownTimer.stop();
        }
    }

    // resumes the countdown timer if the session is not finished
    public void resumeTimer() {
        if (countdownTimer != null && !countdownTimer.isRunning()) {
            countdownTimer.start();
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

    public int getStreak() {
        return streak;
    }
}
