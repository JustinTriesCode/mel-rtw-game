package com.justintriescode.mellysgame.game;

import com.justintriescode.mellysgame.ui.GameRunner;
import com.justintriescode.mellysgame.ui.ResourceLoader;

import javax.swing.Timer;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.List;

public class AlphabetSoup extends BaseMiniGame {
    // font variables
    private float alpha = 0.0f; // For fade effect (0.0 to 1.0)
    private Font customFont;

    // colour palette for letters
    private Color targetColour;
    private Color easyModeColor = new Color(200, 200, 190);
    private final Color[] palette = {
            new Color(0, 255, 255), // Cyan
            new Color(220, 130, 220), // Magenta
            new Color(240, 230, 140), // Yellow
            new Color(255, 165, 0), // Orange
            new Color(100, 149, 237) // Blue
    };

    private final Random random = new Random();
    private final Timer spawnTimer;
    private Timer fadeTimer = null;
    private boolean isWaiting = false;
    private boolean isHardMode;
    private List<GameLetter> activeLetters = new ArrayList<>();
    private int attempts = 0;

    public AlphabetSoup(GameRunner runner, boolean isHardMode, int durationSeconds) {
        super(runner, durationSeconds);

        this.isHardMode = isHardMode;
        this.customFont = ResourceLoader.loadFont("soupFont.ttf", 120f);
        // delay between letters (ms)
        spawnTimer = new Timer(800, e -> spawnNewLetter());
        spawnTimer.setRepeats(false);

        // Timer for the fade-in animation (ms)
        fadeTimer = new Timer(30, e -> {
            alpha += 0.1f;
            if (alpha >= 1.0f) {
                alpha = 1.0f;
                fadeTimer.stop();
            }
            repaint();
        });

        setupKeyListener();
        // add listener to spawn first letter once we have dimensions to work with
        this.addAncestorListener(new javax.swing.event.AncestorListener() {
            @Override
            public void ancestorAdded(javax.swing.event.AncestorEvent event) {
                if (getWidth() > 0) {
                    spawnNewLetter();
                    removeAncestorListener(this);
                }
            }

            @Override
            public void ancestorRemoved(javax.swing.event.AncestorEvent event) {
            }

            @Override
            public void ancestorMoved(javax.swing.event.AncestorEvent event) {
            }
        });
    }

    private void setupKeyListener() {
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (isWaiting)
                    return;
                char pressed = Character.toUpperCase(e.getKeyChar());
                handleInput(pressed);
            }
        });
    }

    private void spawnNewLetter() {
        activeLetters.clear();
        isWaiting = false;
        alpha = 0.0f;

        targetColour = palette[random.nextInt(palette.length)];
        int count = isHardMode ? 3 : 1;

        // track letters to avoid dupes
        java.util.Set<Character> usedCharacters = new java.util.HashSet<>();

        for (int i = 0; i < count; i++) {
            char c = (char) ('A' + random.nextInt(26));
            Point p = getRandomLocation(); // Helper to get x, y

            // Keep picking a new letter until it's unique
            do {
                c = (char) ('A' + random.nextInt(26));
            } while (usedCharacters.contains(c));

            Color letterColor;
            if (!isHardMode) {
                // easy mode only uses 1 colour
                letterColor = easyModeColor;
                targetColour = easyModeColor;
            } else if (i == 0) {
                letterColor = targetColour;
            } else {
                do {
                    letterColor = palette[random.nextInt(palette.length)];
                } while (letterColor.equals(targetColour));
            }
            activeLetters.add(new GameLetter(c, p, letterColor));
        }

        // Shuffle which is first one drawn
        Collections.shuffle(activeLetters);

        fadeTimer.start();
    }

    @Override
    public void handleInput(Object input) {
        char pressed = (char) input;
        boolean foundCorrect = false;

        for (GameLetter gl : activeLetters) {
            if (gl.character == pressed && gl.color.equals(targetColour)) {
                foundCorrect = true;
                break;
            }
        }

        if (foundCorrect) {
            int baseValue = 10;
            if (alpha < 0.5f) {
                baseValue += 5; // speed bonus
            }
            if (attempts > 2) {
                baseValue -= 20; // attempt penalty
            } else if (attempts > 0) {
                baseValue -= 10;
            }
            attempts = 0;
            session.addPointsWithMultiplier(baseValue, isHardMode ? 3 : 1);
            statusMessage = "Match!";
            isWaiting = true;
            spawnTimer.start();
        } else {
            statusMessage = "Wrong letter!";
            messageTimer.restart();
            attempts++;
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        drawScore(g2);
        drawTimer(g2);
        drawStatusMessage(g2);

        // Draw the Center Sphere, colour depends on difficulty
        if (isHardMode) {
            g2.setColor(targetColour);
        } else {
            g2.setColor(getBackground());
        }
        int sphereSize = 60;
        g2.fillOval(getWidth() / 2 - sphereSize / 2, getHeight() / 2 - sphereSize / 2, sphereSize, sphereSize);

        // Draw all active letters
        if (!isWaiting) {
            g2.setFont(customFont);
            for (GameLetter gl : activeLetters) {
                // Apply current alpha for the fade-in effect
                g2.setColor(new Color(
                        gl.color.getRed() / 255f,
                        gl.color.getGreen() / 255f,
                        gl.color.getBlue() / 255f,
                        alpha));
                g2.drawString(String.valueOf(gl.character), gl.position.x, gl.position.y);
            }
        }
    }

    private Point getRandomLocation() {
        int maxAttempts = 100;
        int margin = 120; // Distance between letters
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;

        for (int i = 0; i < maxAttempts; i++) {
            int x = 110 + random.nextInt(Math.max(1, getWidth() - 200));
            int y = 150 + random.nextInt(Math.max(1, getHeight() - 250));
            Point candidate = new Point(x, y);

            boolean tooClose = false;

            // Check distance from Score and Timer
            if (x > getWidth() - 300 && y < 150)
                tooClose = true;
            if (x < 300 && y > getHeight() - 150)
                tooClose = true;
            // Check distance from Center Sphere
            if (candidate.distance(centerX, centerY) < 150) {
                tooClose = true;
            }
            // Check distance from other spawned letters
            for (GameLetter gl : activeLetters) {
                if (candidate.distance(gl.position) < margin) {
                    tooClose = true;
                    break;
                }
            }

            if (!tooClose)
                return candidate;
        }
        return new Point(getWidth() / 2, 200); // fall back if can't find spot after maxAttempts
    }

    @Override
    public void resetGame() {
        spawnTimer.stop();
        fadeTimer.stop();
        spawnNewLetter();
    }

    private class GameLetter {
        char character;
        Point position;
        Color color;

        GameLetter(char character, Point position, Color color) {
            this.character = character;
            this.position = position;
            this.color = color;
        }
    }

    @Override
    protected void stopSpecificTimers() {
        if (spawnTimer != null)
            spawnTimer.stop();
        if (fadeTimer != null)
            fadeTimer.stop();
    }
}