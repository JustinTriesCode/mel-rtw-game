package com.justintriescode.mellysgame.ui;

import com.justintriescode.mellysgame.game.PlayerProfile;
import com.justintriescode.mellysgame.data.SessionRecord;
import com.justintriescode.mellysgame.model.Stats;
import com.justintriescode.mellysgame.data.DataManager;
import com.justintriescode.mellysgame.events.Event;
import com.justintriescode.mellysgame.events.EventLog;

import java.util.List;

import javax.swing.*;
import java.awt.*;

/**
 * A UI panel that displays the player's lifetime statistics.
 * Designed to be embedded inside a generic dialog window.
 */
public class StatsPanel extends JPanel {
    private GameRunner runner;

    /**
     * Initializes the StatsPanel with the player's profile data and stats
     * 
     * @param runner The GameRunner instance to access the player profile and show
     *               dialogs
     */
    public StatsPanel(GameRunner runner) {
        this.runner = runner;
        PlayerProfile profile = runner.getPlayerProfile();

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);

        addStatRow("Top Score:", String.valueOf(profile.getTopScore()));

        double avg = profile.getAverageScorePerSession();
        addStatRow("Avg Score/Session:", String.format("%.0f", avg));
        addGraphSubStatRow("Number Game Avg.: ", String.valueOf(Stats.getAverageScoreByGameType(profile, true)), true);
        addGraphSubStatRow("Letter Game Avg.: ", String.valueOf(Stats.getAverageScoreByGameType(profile, false)),
                false);

        addStatRow("Lifetime Score:", String.valueOf(profile.getTotalLifetimeScore()));
        addStatRow("Total Sessions:", String.valueOf(profile.getTotalSessionsPlayed()));
        addSubStatRow("Total Time Played:", formatTime(profile.getTotalLifetimeSeconds()));

        List<SessionRecord> history = profile.getSessionHistory();
        if (!history.isEmpty()) {
            SessionRecord lastGame = history.get(history.size() - 1);
            String difficulty = lastGame.isHard() ? "Hard" : "Easy";
            addStatRow("Last Game Played: ", lastGame.getGameName());
            addSubStatRow("Difficulty:", difficulty);
            addSubStatRow("Score:", lastGame.getScore() + " pts");
        }

        add(Box.createVerticalStrut(20));
        JButton detailsBtn = new JButton("View Breakdown by Symptom");
        UIStyleUtils.formatButton(detailsBtn, 18);
        detailsBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        detailsBtn.addActionListener(e -> showDetailedStats(profile));
        add(detailsBtn);

        add(Box.createVerticalStrut(20));
        JButton resetBtn = new JButton("Reset All Stats");
        UIStyleUtils.formatButton(resetBtn, 18);
        resetBtn.setBackground(new Color(220, 120, 120));
        resetBtn.setForeground(Color.WHITE);
        resetBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        resetBtn.addActionListener(e -> handleResetStats());
        add(resetBtn);
    }

    /**
     * Adds a standard, top-level statistic row to the panel.
     *
     * @param label The label for the statistic (e.g., "Top Score:").
     * @param value The value to display.
     */
    private void addStatRow(String label, String value) {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);

        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Serif", Font.BOLD, 22));
        lbl.setForeground(UIStyleUtils.TEXT_DARK);

        JLabel val = new JLabel(value);
        val.setFont(new Font("Serif", Font.PLAIN, 22));
        val.setForeground(UIStyleUtils.TEXT_DARK);

        row.add(lbl, BorderLayout.WEST);
        row.add(val, BorderLayout.EAST);
        row.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 50));

        add(row);
    }

    /**
     * Adds an indented sub-statistic row with a smaller, italicized font.
     *
     * @param label The label for the sub-statistic.
     * @param value The value to display.
     */
    private void addSubStatRow(String label, String value) {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);

        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Serif", Font.ITALIC, 18));
        lbl.setForeground(UIStyleUtils.TEXT_DARK);

        JLabel val = new JLabel(value);
        val.setFont(new Font("Serif", Font.PLAIN, 18));
        val.setForeground(UIStyleUtils.TEXT_DARK);

        row.add(lbl, BorderLayout.WEST);
        row.add(val, BorderLayout.EAST);
        row.setBorder(BorderFactory.createEmptyBorder(2, 40, 5, 50));

        add(row);
    }

    /**
     * Adds an indented sub-statistic row that includes a clickable graph icon.
     *
     * @param label         The label for the sub-statistic.
     * @param value         The value to display.
     * @param isNumberGames True if the graph should display number games, false for
     *                      letter games.
     */
    private void addGraphSubStatRow(String label, String value, boolean isNumberGames) {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);

        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Serif", Font.ITALIC, 18));
        lbl.setForeground(UIStyleUtils.TEXT_DARK);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightPanel.setOpaque(false);

        JLabel val = new JLabel(value);
        val.setFont(new Font("Serif", Font.PLAIN, 18));
        val.setForeground(UIStyleUtils.TEXT_DARK);

        JButton graphBtn = new JButton(UIStyleUtils.BAR_ICON);
        graphBtn.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
        graphBtn.setMargin(new Insets(2, 5, 2, 5));
        graphBtn.setFocusPainted(false);
        graphBtn.setToolTipText("View Graph");
        graphBtn.addActionListener(e -> showGraphDialog(isNumberGames));

        rightPanel.add(val);
        rightPanel.add(graphBtn);

        row.add(lbl, BorderLayout.WEST);
        row.add(rightPanel, BorderLayout.EAST);
        row.setBorder(BorderFactory.createEmptyBorder(2, 40, 5, 50));

        add(row);
    }

    /**
     * Displays a nested dialog with a detailed breakdown of stats by symptom
     * severity.
     *
     * @param profile The player's profile data to extract detailed stats from.
     */
    private void showDetailedStats(PlayerProfile profile) {
        JPanel detailPanel = new JPanel();
        detailPanel.setLayout(new BoxLayout(detailPanel, BoxLayout.Y_AXIS));
        detailPanel.setOpaque(false);

        String[] severities = { "None", "Mild", "Moderate", "Severe" };
        for (String severity : severities) {
            int games = Stats.getGamesPlayedBySeverity(profile, severity);
            int highScore = Stats.getHighScoreBySeverity(profile, severity);

            JPanel row = new JPanel(new BorderLayout());
            row.setOpaque(false);

            JLabel lbl = new JLabel(severity + " (" + games + " games)");
            lbl.setFont(new Font("Serif", Font.BOLD, 20));
            lbl.setForeground(UIStyleUtils.TEXT_DARK);

            JLabel val = new JLabel("Top Score: " + highScore);
            val.setFont(new Font("Serif", Font.PLAIN, 20));
            val.setForeground(UIStyleUtils.TEXT_DARK);

            row.add(lbl, BorderLayout.WEST);
            row.add(val, BorderLayout.EAST);
            row.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 50));
            detailPanel.add(row);
        }

        runner.showGenericDialog("Symptom Breakdown", detailPanel, () -> {
        });
    }

    /**
     * Displays a nested dialog containing a bar chart of average scores.
     *
     * @param isNumberGames True to show the chart for number games, false for
     *                      letter games.
     */
    private void showGraphDialog(boolean isNumberGames) {
        JPanel graphContainer = new JPanel(new BorderLayout());
        graphContainer.setOpaque(false);
        String gameName = isNumberGames ? "Equationista" : "AlphabetSoup";
        graphContainer.add(new BarChartPanel(runner.getPlayerProfile(), gameName), BorderLayout.CENTER);
        String title = isNumberGames ? "Number Games Averages" : "Letter Games Averages";
        runner.showGenericDialog(title, graphContainer, () -> {
        });
    }

    /**
     * Prompts the user for confirmation and, if confirmed, resets all player
     * statistics,
     * saves the blank profile, and closes the stats dialog.
     */
    private void handleResetStats() {
        int choice = JOptionPane.showConfirmDialog(
                runner,
                "Are you sure you want to reset all your stats?\nThis action cannot be undone.",
                "Confirm Reset",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (choice == JOptionPane.YES_OPTION) {
            PlayerProfile profile = runner.getPlayerProfile();
            profile.resetStats();
            try {
                DataManager.save(profile);
                // Find the parent dialog and close it
                Window parentWindow = SwingUtilities.getWindowAncestor(this);
                if (parentWindow != null) {
                    parentWindow.dispose();
                }
                JOptionPane.showMessageDialog(
                        runner,
                        "All player stats have been reset.",
                        "Reset Complete",
                        JOptionPane.PLAIN_MESSAGE);
            } catch (java.io.IOException ex) {
                EventLog.getInstance().addEvent(new Event("Failed to save profile after reset", ex));
                JOptionPane.showMessageDialog(
                        runner,
                        "Could not save the reset profile. Please try again.",
                        "Error",
                        JOptionPane.PLAIN_MESSAGE);
            }
        }
    }

    /**
     * Formats a total number of seconds into a readable string (e.g., "1h 5m 30s"
     * or "5m 30s").
     *
     * @param totalSeconds The total seconds to format.
     * @return A formatted time string.
     */
    private String formatTime(int totalSeconds) {
        int hours = totalSeconds / 3600;
        int minutes = (totalSeconds % 3600) / 60;
        int seconds = totalSeconds % 60;

        if (hours > 0) {
            return String.format("%dh %dm %ds", hours, minutes, seconds);
        }
        return String.format("%dm %ds", minutes, seconds);
    }
}
