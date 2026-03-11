package com.justintriescode.mellysgame.game;

import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import com.justintriescode.mellysgame.model.Playable;
import com.justintriescode.mellysgame.ui.GameRunner;
import com.justintriescode.mellysgame.game.GameSession;
import com.justintriescode.mellysgame.ui.UIStyleUtils;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Window;

public abstract class BaseMiniGame extends JPanel implements Playable {
    protected GameRunner runner;
    protected GameSession session;
    protected String statusMessage = "";
    protected Timer messageTimer;
    protected boolean hardMode;
    protected boolean isGameRunning = false;

    public BaseMiniGame(GameRunner runner, boolean hardMode, int durationSeconds) {
        this.runner = runner;
        this.hardMode = hardMode;
        this.setBackground(new Color(45, 45, 45));
        this.setFocusable(true);
        this.session = new GameSession(new PlayerProfile(), durationSeconds, this::handleGameOver, this);

        // escape key to return to menu
        this.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ESCAPE"), "openPauseMenu");
        this.getActionMap().put("openPauseMenu", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                handleEscape();
            }
        });

        messageTimer = new Timer(1500, e -> {
            statusMessage = "";
            repaint();
        });
        messageTimer.setRepeats(false);
    }

    protected void handleGameOver() {
        runner.showGameOver(session.getSessionScore(), session.getStreak());
    }

    // Shared UI helpers
    // EFFECTS: draws the score on the screen
    protected void drawScore(Graphics2D g2) {
        simpleFontSetting(g2);

        String scoreText = "Score: " + session.getSessionScore();
        FontMetrics metrics = g2.getFontMetrics();
        int x = getWidth() - metrics.stringWidth(scoreText) - 40;
        int y = 50;

        g2.drawString(scoreText, x, y);
    }

    protected void drawTimer(Graphics2D g2) {
        simpleFontSetting(g2);
        g2.drawString("Time: " + session.getTimeString(), 30, getHeight() - 50);
    }

    protected void simpleFontSetting(Graphics2D g2) {
        g2.setFont(new Font("Serif", Font.PLAIN, 28));
        g2.setColor(new Color(100, 100, 90));
    }

    // EFFECTS: draws the status message at the bottom of the screen
    protected void drawStatusMessage(Graphics2D g2) {
        if (statusMessage.isEmpty())
            return;
        g2.setFont(new Font("Serif", Font.PLAIN, 28));

        if (statusMessage.contains("Correct") || statusMessage.contains("Great work")) {
            g2.setColor(new Color(100, 140, 100));
        } else {
            g2.setColor(new Color(160, 100, 100));
        }

        FontMetrics metrics = g2.getFontMetrics();
        int x = (getWidth() - metrics.stringWidth(statusMessage)) / 2;
        int y = getHeight() - 60;

        g2.drawString(statusMessage, x, y);
    }

    // EFFECTS: stops the game session's active timers
    public void stopGame() {
        isGameRunning = false;
        if (session != null) {
            session.abort();
        }

        if (messageTimer != null)
            messageTimer.stop();
        // For AlphabetSoup specifically:
        stopSpecificTimers();

        runner.handleSessionEnd(
                this.getClass().getSimpleName(),
                this.hardMode,
                session.getSessionScore(),
                session.getStreak());
    }

    protected void stopSpecificTimers() {
    }

    private void handleEscape() {
        session.pauseTimer();

        JPanel pausePanel = new JPanel();
        pausePanel.setLayout(new BoxLayout(pausePanel, BoxLayout.Y_AXIS));
        pausePanel.setOpaque(false);

        JLabel label = new JLabel("Game Paused. Would you like to quit?");
        label.setFont(new Font("Serif", Font.PLAIN, 20));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        pausePanel.add(label);

        // resume button
        JButton resumeBtn = new JButton("Resume Game");
        UIStyleUtils.formatButton(resumeBtn, 20);
        resumeBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        resumeBtn.addActionListener(e -> {
            Window w = SwingUtilities.getWindowAncestor(resumeBtn);
            if (w != null)
                w.dispose();
        });
        pausePanel.add(Box.createVerticalStrut(20));
        pausePanel.add(resumeBtn);

        runner.showGenericDialog("Paused", pausePanel, () -> {
            this.isGameRunning = false;
            stopGame();
            runner.showScreen("MENU");
        });

        if (this.isDisplayable()) {
            session.resumeTimer();
        }
    }
}