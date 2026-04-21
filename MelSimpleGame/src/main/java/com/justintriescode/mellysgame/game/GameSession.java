package com.justintriescode.mellysgame.game;

import javax.swing.Timer;

import com.justintriescode.mellysgame.events.Observable;

/**
 * Represents an active session of a mini-game.
 * Manages game timers, score calculation, streak tracking, and player
 * statistics.
 */
public class GameSession extends Observable<GameSession> {
    // for global score/stats
    private final PlayerProfile profile;

    // for current session only
    private int sessionScore;
    private int secondsRemaining;
    private Timer countdownTimer;
    private Runnable onTimeUp;
    private boolean isFinished = false; // need to add a check for this later
    private int streak = 0;
    private boolean isActive = true;

    // for point calculation logic
    private static final int BASE_POINTS = 10;
    private static final int PERFECT_BONUS = 40;
    private static final int PENALTY_PER_ATTEMPT = 15;
    private static final int STREAK_THRESHOLD_MID = 10;
    private static final int STREAK_BONUS_MID = 16;

    /**
     * Initializes a new game session with the given duration and callback
     * for when time is up.
     *
     * @param profile  The player profile to update.
     * @param duration The duration of the session in seconds.
     * @param onTimeUp A Runnable callback triggered when the countdown reaches
     *                 zero.
     */
    public GameSession(PlayerProfile profile, int duration, Runnable onTimeUp) {
        this.profile = profile;
        this.sessionScore = 0;
        this.secondsRemaining = duration;
        this.streak = 0;
        this.onTimeUp = onTimeUp;
        this.countdownTimer = new Timer(1000, e -> {
            tick();
        });
        this.countdownTimer.start();
    }

    /**
     * Handles the countdown logic for each tick of the timer.
     */
    private void tick() {
        if (!isActive) {
            return;
        }
        if (secondsRemaining > 0) {
            secondsRemaining--;
            profile.increaseLifetimeSeconds();
            notifyObservers(this);
        } else {
            isActive = false;
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
        earnedPoints = Math.max(earnedPoints, -PENALTY_PER_ATTEMPT * difficultyMultiplier);
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
        notifyObservers(this);
    }

    /**
     * Processes a milestone achievement, awarding points based on the difficulty
     * multiplier and the intensity of the milestone.
     * This incentivizes players to reach certain milestones in the game.
     * 
     * @param difficultyMultiplier The multiplier from the current game mode.
     * @param intensity            A measure of how significant the milestone is
     *                             (e.g.,
     *                             number of cycles completed).
     */
    public void processMilestone(double difficultyMultiplier, int intensity) {
        int milestoneBase = 10;
        double totalBonus = (milestoneBase * intensity) * difficultyMultiplier;
        applyPoints((int) Math.round(totalBonus));
    }

    /**
     * Applies a bonus for achieving a "perfect" cycle, incentivizing players
     * to increase the game's difficulty.
     * 
     * @param difficultyIncreases The number of times the difficulty has been
     *                            increased.
     */
    public void processPerfectionBonus(int difficultyIncreases) {
        int finalBonus = (int) Math.round(PERFECT_BONUS * difficultyIncreases);
        applyPoints(finalBonus);
    }

    /**
     * Ends the current game session, stops the timer, updates the profile stats,
     * and triggers the time-up callback.
     */
    private void endSession() {
        isFinished = true;
        countdownTimer.stop();
        if (onTimeUp != null)
            onTimeUp.run();
    }

    /**
     * Pauses the countdown timer to prevent it from running in the background.
     */
    public void pauseTimer() {
        if (countdownTimer != null) {
            countdownTimer.stop();
        }
    }

    /**
     * Resumes the countdown timer if the session is not finished.
     */
    public void resumeTimer() {
        if (countdownTimer != null && !countdownTimer.isRunning()) {
            countdownTimer.start();
        }
    }

    public void abort() {
        this.isActive = false;
        if (countdownTimer != null) {
            countdownTimer.stop();
        }
        this.onTimeUp = null;
    }

    // Getters and setters
    /**
     * Updates the duration of the current session.
     * If the provided duration is 0 or less, it defaults to 1 minute (60 seconds).
     *
     * @param newDuration The new duration in seconds.
     */
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
