package com.justintriescode.mellysgame.game;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.justintriescode.mellysgame.data.SessionRecord;

public class PlayerProfile {
    private int totalSessionsPlayed = 0;
    private int totalLifetimeScore;
    private int totalLifetimeSeconds;

    // TODO: setup logic for top score and name, and add to ui to update name
    private String topScorerName = "AAA";
    private int topScore = 0;
    private Map<LocalDate, Integer> dailyHistory = new HashMap<>();

    // EFFECTS: Logic to update stats
    public void updateStats(int sessionScore) {
        this.totalLifetimeScore += sessionScore;
        LocalDate today = LocalDate.now();
        dailyHistory.put(today, dailyHistory.getOrDefault(today, 0) + sessionScore);

        // Check for new top score
        if (sessionScore > topScore) {
            this.topScore = sessionScore;
            // TODO: prompt for name input and update topScorerName somewhere in the ui
        }
    }

    public void recordSession(int sessionScore, long sessionSeconds) {
        this.totalSessionsPlayed++;
        this.totalLifetimeScore += sessionScore;
        this.totalLifetimeSeconds += sessionSeconds;

        LocalDate today = LocalDate.now();
        int currentDayTotal = dailyHistory.getOrDefault(today, 0);
        dailyHistory.put(today, currentDayTotal + sessionScore);
    }

    @JsonIgnore
    public double getAverageScorePerSession() {
        if (totalSessionsPlayed == 0)
            return 0.0;
        return (double) totalLifetimeScore / totalSessionsPlayed;
    }

    public void increaseLifetimeSeconds() {
        this.totalLifetimeSeconds++;
    }

    public void increaseLifetimeScore(int points) {
        this.totalLifetimeScore += points;
    }

    // TODO: add any missing getters and Setters (Jackson needs these or public
    // fields)

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

    // session data
    private List<SessionRecord> sessionHistory = new ArrayList<>();

    public void recordSessionContext(String game, String severity, boolean hard, int score) {
        this.totalSessionsPlayed++;
        this.totalLifetimeScore += score;
        this.sessionHistory.add(new SessionRecord(game, severity, hard, score));

        LocalDate today = LocalDate.now();
        dailyHistory.put(today, dailyHistory.getOrDefault(today, 0) + score);
    }

    public List<SessionRecord> getSessionHistory() {
        return sessionHistory;
    }

    public void setSessionHistory(List<SessionRecord> history) {
        this.sessionHistory = history;
    }
}
