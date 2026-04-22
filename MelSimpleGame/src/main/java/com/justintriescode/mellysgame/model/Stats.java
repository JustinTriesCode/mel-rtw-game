package com.justintriescode.mellysgame.model;

import com.justintriescode.mellysgame.data.SessionRecord;
import com.justintriescode.mellysgame.game.PlayerProfile;

/**
 * Utility class for calculating specific statistics from a player's profile.
 */
public class Stats {

    /**
     * Calculates the highest score achieved under a specific symptom severity.
     *
     * @param profile  The player's profile containing session history.
     * @param severity The symptom severity to filter by (e.g., "Mild", "Severe").
     * @return The highest score achieved for the specified severity, or 0 if none
     *         are found.
     */
    public static int getHighScoreBySeverity(PlayerProfile profile, String severity) {
        int highScore = 0;
        for (SessionRecord record : profile.getSessionHistory()) {
            if (record.getSeverity().equalsIgnoreCase(severity) && record.getScore() > highScore) {
                highScore = record.getScore();
            }
        }
        return highScore;
    }

    /**
     * Calculates the average score for a given game, severity, and difficulty.
     * 
     * @param profile  The player's profile containing session history.
     * @param severity The symptom severity to filter by (e.g., "Mild", "Severe").
     * @param gameName The name of the mini-game to filter by (e.g., "LetterSoup").
     * @param isHard   True to filter for hard mode sessions, false for easy mode.
     * @return The average score for the specified criteria, or 0 if no matching
     *         sessions are found.
     */
    public static int getAverageScoreBySeverityAndDifficulty(PlayerProfile profile, String severity, String gameName,
            boolean isHard) {
        int totalScore = 0;
        int count = 0;
        for (SessionRecord record : profile.getSessionHistory()) {
            if (record.getSeverity().equalsIgnoreCase(severity)
                    && record.getGameName().equalsIgnoreCase(gameName) && record.isHard() == isHard) {
                totalScore += record.getScore();
                count++;
            }
        }
        return count > 0 ? totalScore / count : 0;
    }

    /**
     * Calculates the average score for a specific game type (number-based:
     * Equationista/Numberista, or letter-based: AlphabetSoup/LetterSoup) across all
     * sessions.
     * 
     * @param profile     The player's profile containing session history.
     * @param numberGames True to calculate for number-based games, false for
     *                    letter-based games.
     * @return The average score for the specified game type, or 0 if no matching
     *         sessions are found.
     */
    public static int getAverageScoreByGameType(PlayerProfile profile, Boolean numberGames) {
        int totalScore = 0;
        int count = 0;
        if (numberGames) {
            for (SessionRecord record : profile.getSessionHistory()) {
                if (record.getGameName().equalsIgnoreCase("Equationista")) {
                    totalScore += record.getScore();
                    count++;
                }
            }
        } else {
            for (SessionRecord record : profile.getSessionHistory()) {
                if (record.getGameName().equalsIgnoreCase("AlphabetSoup")) {
                    totalScore += record.getScore();
                    count++;
                }
            }
        }
        return count > 0 ? totalScore / count : 0;
    }

    /**
     * Counts the total number of games played under a specific symptom severity.
     * 
     * @param profile  The player's profile containing session history.
     * @param severity The symptom severity to filter by (e.g., "Mild", "Severe").
     * @return The total number of games played under the specified severity.
     */
    public static int getGamesPlayedBySeverity(PlayerProfile profile, String severity) {
        int count = 0;
        for (SessionRecord record : profile.getSessionHistory()) {
            if (record.getSeverity().equalsIgnoreCase(severity)) {
                count++;
            }
        }
        return count;
    }
}
