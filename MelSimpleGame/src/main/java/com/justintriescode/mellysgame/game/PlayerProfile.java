package com.justintriescode.mellysgame.game;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.justintriescode.mellysgame.data.SessionRecord;
import com.justintriescode.mellysgame.events.Observable;

/**
 * Represents the persistent profile and statistics of a player.
 * This class tracks lifetime scores, play time, daily history,
 * and individual session records. It is serialized and deserialized
 * using Jackson for saving and loading player data.
 */
public class PlayerProfile extends Observable<PlayerProfile> {
    private int totalSessionsPlayed = 0;
    private int totalLifetimeScore;
    private int totalLifetimeSeconds;
    private String lastRecordedSeverity = "None";

    // TODO: setup logic for top score and name, and add to ui to update name
    private String topScorerName = "AAA";
    private int topScore = 0;
    private Map<LocalDate, Integer> dailyHistory = new HashMap<>();

    /**
     * Calculates the average score across all played sessions.
     * This field is ignored by Jackson during JSON serialization.
     *
     * @return The average score, or 0.0 if no sessions have been played.
     */
    @JsonIgnore
    public double getAverageScorePerSession() {
        if (totalSessionsPlayed == 0)
            return 0.0;
        return (double) totalLifetimeScore / totalSessionsPlayed;
    }

    /**
     * Increments the total lifetime seconds played by one.
     */
    public void increaseLifetimeSeconds() {
        this.totalLifetimeSeconds++;
    }

    // Getters and setters
    public int getTotalLifetimeScore() {
        return totalLifetimeScore;
    }

    public void setTotalLifetimeScore(int s) {
        this.totalLifetimeScore = s;
    }

    public int getTopScore() {
        return topScore;
    }

    public void setTopScore(int s) {
        this.topScore = s;
    }

    public String getTopScorerName() {
        return topScorerName;
    }

    public void setTopScorerName(String name) {
        this.topScorerName = name;
    }

    public Map<LocalDate, Integer> getDailyHistory() {
        return dailyHistory;
    }

    public void setDailyHistory(Map<LocalDate, Integer> h) {
        this.dailyHistory = h;
    }

    public int getTotalSessionsPlayed() {
        return totalSessionsPlayed;
    }

    public void setTotalSessionsPlayed(int totalSessionsPlayed) {
        this.totalSessionsPlayed = totalSessionsPlayed;
    }

    public int getTotalLifetimeSeconds() {
        return totalLifetimeSeconds;
    }

    public void setTotalLifetimeSeconds(int totalLifetimeSeconds) {
        this.totalLifetimeSeconds = totalLifetimeSeconds;
    }

    public String getLastRecordedSeverity() {
        return lastRecordedSeverity;
    }

    public void setLastRecordedSeverity(String s) {
        this.lastRecordedSeverity = s;
    }

    // session data
    private List<SessionRecord> sessionHistory = new ArrayList<>();

    /**
     * Records the full context of a completed game session and updates all relevant
     * statistics,
     * including total sessions, lifetime score, session history, daily history, and
     * top score.
     *
     * @param game     The name of the mini-game played.
     * @param severity The recorded symptom severity during the session.
     * @param hard     True if the session was played on hard mode, false otherwise.
     * @param score    The final score achieved in the session.
     */
    public void recordSessionContext(String game, String severity, boolean hard, int score) {
        this.totalSessionsPlayed++;
        this.totalLifetimeScore += score;
        this.sessionHistory.add(new SessionRecord(game, severity, hard, score));

        LocalDate today = LocalDate.now();
        dailyHistory.put(today, dailyHistory.getOrDefault(today, 0) + score);

        if (score > topScore) {
            this.topScore = score;
            notifyObservers(this);
        }
    }

    public List<SessionRecord> getSessionHistory() {
        return sessionHistory;
    }

    public void setSessionHistory(List<SessionRecord> history) {
        this.sessionHistory = history;
    }

    /**
     * Resets all player statistics to their default, empty state.
     */
    public void resetStats() {
        this.totalSessionsPlayed = 0;
        this.totalLifetimeScore = 0;
        this.totalLifetimeSeconds = 0;
        this.topScore = 0;
        this.topScorerName = "AAA";
        this.sessionHistory.clear();
        this.dailyHistory.clear();
    }
}
