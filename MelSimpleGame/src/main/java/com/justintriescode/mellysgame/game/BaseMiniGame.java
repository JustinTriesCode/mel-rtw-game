package com.justintriescode.mellysgame.game;

import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.Timer;
import static javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW;

import com.justintriescode.mellysgame.model.Playable;
import com.justintriescode.mellysgame.ui.GameRunner;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

public abstract class BaseMiniGame extends JPanel implements Playable {
    protected GameRunner runner;
    protected GameSession session;
    protected String statusMessage = "";
    protected Timer messageTimer;

    public BaseMiniGame(GameRunner runner, int durationSeconds) {
        this.runner = runner;
        this.setBackground(new Color(45, 45, 45));
        this.setFocusable(true);
        this.session = new GameSession(new PlayerProfile(), durationSeconds, this::handleGameOver, this);

        // escape key to return to menu
        this.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ESCAPE"), "backToMenu");
        this.getActionMap().put("backToMenu", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                stopGame();
                runner.showScreen("MENU");
            }
        });

        messageTimer = new Timer(1500, e -> {
            statusMessage = "";
            repaint();
        });
        messageTimer.setRepeats(false);
    }

    protected void handleGameOver() {
        JOptionPane.showMessageDialog(this, "Time's up! Score: " + session.getSessionScore());
        runner.showScreen("MENU");
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
        if (session != null) {
            session.pauseTimer();
        }
        if (messageTimer != null)
            messageTimer.stop();
        // For AlphabetSoup specifically:
        stopSpecificTimers();
    }

    protected void stopSpecificTimers() {
    }
}