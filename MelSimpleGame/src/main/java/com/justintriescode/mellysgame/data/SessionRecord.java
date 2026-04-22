package com.justintriescode.mellysgame.data;

import java.time.LocalDate;

/**
 * Represents a record of a game session, including the date, game name,
 * severity level, difficulty, and score.
 */
public class SessionRecord {
    private String date;
    private String gameName;
    private String severity;
    private boolean isHard;
    private int score;

    public SessionRecord() {
    }

    public SessionRecord(String gameName, String severity, boolean isHard, int score) {
        this.date = LocalDate.now().toString();
        this.gameName = gameName;
        this.severity = severity;
        this.isHard = isHard;
        this.score = score;
    }

    public String getDate() {
        return date;
    }

    public String getGameName() {
        return gameName;
    }

    public String getSeverity() {
        return severity;
    }

    public boolean isHard() {
        return isHard;
    }

    public int getScore() {
        return score;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public void setHard(boolean isHard) {
        this.isHard = isHard;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
