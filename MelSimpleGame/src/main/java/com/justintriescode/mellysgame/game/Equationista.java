package com.justintriescode.mellysgame.game;

import com.justintriescode.mellysgame.model.Equation;
import com.justintriescode.mellysgame.ui.GameRunner;
import com.justintriescode.mellysgame.ui.ResourceLoader;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Random;
import com.justintriescode.mellysgame.events.Event;
import com.justintriescode.mellysgame.events.EventLog;

/**
 * Represents the Equationista mini-game where players select the smallest value
 * from a set of equations.
 * Players use arrow keys (or WASD) to select the direction corresponding to the
 * smallest value.
 * The game features a dynamic difficulty system that adjusts the range of
 * generated equations based on player performance.
 */
public class Equationista extends BaseMiniGame {
    private int difficultyIncreases = 0;
    private boolean hardMode = false;
    private HashMap<String, Equation> optionsMap = new HashMap<>();
    private int max = 100;
    private BufferedImage arrowImage;
    private int attemptsThisRound = 0;
    private Random random = new Random();
    private int cycleCount = 0;
    private int triesThisCycleCount = 0;
    private int targetCycleCount;
    private static final double HARD_MODE_SCALE = 2.0;
    private static final double EASY_MODE_SCALE = 0.18; // Reduced to balance target score around 1300

    /**
     * Initializes the game, sets up key bindings, and starts the game session
     * timer.
     *
     * @param runner          The GameRunner instance for screen navigation and
     *                        shared state.
     * @param hardMode        True if hard mode is enabled, false for easy mode.
     * @param maxRange        The maximum value range for generated equations.
     * @param durationSeconds The duration of the session in seconds.
     */
    public Equationista(GameRunner runner, boolean hardMode, int maxRange, int durationSeconds) {
        super(runner, hardMode, durationSeconds);

        setBackground(new Color(45, 45, 45));
        this.hardMode = hardMode;
        this.max = maxRange;
        this.targetCycleCount = updateTargetCycleCount();
        arrowImage = ResourceLoader.loadImage("arrowKeys.png");

        // Initialize the HashMap with arrow directions and corresponding equations
        updateOptions();
        keyListener();
        resizeImg(1);
    }

    /**
     * Lays out key listeners to represent arrow directions (Up, Down, Left, Right)
     * and WASD.
     */
    private void keyListener() {
        this.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent e) {
                int code = e.getKeyCode();
                if (code == java.awt.event.KeyEvent.VK_UP || code == java.awt.event.KeyEvent.VK_W)
                    handleInput("Up");
                if (code == java.awt.event.KeyEvent.VK_DOWN || code == java.awt.event.KeyEvent.VK_S)
                    handleInput("Down");
                if (code == java.awt.event.KeyEvent.VK_LEFT || code == java.awt.event.KeyEvent.VK_A)
                    handleInput("Left");
                if (code == java.awt.event.KeyEvent.VK_RIGHT || code == java.awt.event.KeyEvent.VK_D)
                    handleInput("Right");
            }
        });
    }

    /**
     * Resizes the arrow keys image based on the given scale factor.
     *
     * @param scale The factor by which to scale the image.
     */
    public void resizeImg(double scale) {
        BufferedImage original = ResourceLoader.loadImage("arrowKeys.png");

        if (original == null) {
            EventLog.getInstance().addEvent(new Event("CRITICAL: Could not find arrowKeys.png at the specified path!"));
            return;
        }

        int w = (int) (original.getWidth() * scale);
        int h = (int) (original.getHeight() * scale);
        Image scaled = original.getScaledInstance(w, h, Image.SCALE_SMOOTH);

        this.arrowImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = this.arrowImage.createGraphics();
        g2d.drawImage(scaled, 0, 0, null);
        g2d.dispose();
    }

    /**
     * Paints the game screen with the arrow image and equations.
     *
     * @param g The graphics context used for painting.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        drawGameAssets(g2);
        drawScore(g2);
        drawCycleLeft(g2);
        drawTimer(g2);
    }

    /**
     * Helper to draw the main game assets (arrow image and equations).
     *
     * @param g2 The Graphics2D context.
     */
    private void drawGameAssets(Graphics2D g2) {
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        int imgW = arrowImage.getWidth();
        int imgH = arrowImage.getHeight();
        int x = centerX - (imgW / 2);
        int y = centerY - (imgH / 2);

        // Draw the image at the center
        g2.drawImage(arrowImage, x, y, null);

        // Font and color for text
        g2.setColor(new Color(248, 247, 242));
        g2.setFont(new Font("Serif", Font.BOLD, 48));
        g2.drawString("Select the smallest value!", 20, 40);
        FontMetrics fontMetrics = g2.getFontMetrics();
        int fontHeight = fontMetrics.getHeight();
        int padding = fontHeight / 2;

        // Draw the Equations
        drawCenteredString(g2, "Up", centerX, y - padding);
        drawCenteredString(g2, "Down", centerX, y + imgH + fontHeight);
        drawCenteredString(g2, "Left", x - padding * 3, centerY + (imgH / 3));
        drawCenteredString(g2, "Right", x + imgW + padding * 3, centerY + (imgH / 3));
    }

    /**
     * Helper to safely retrieve the text of an equation for the paintComponent.
     *
     * @param direction The direction corresponding to the equation.
     * @return The string representation of the equation, or "?" if missing.
     */
    private String getEqText(String direction) {
        Equation eq = optionsMap.get(direction);
        return (eq != null) ? eq.toString() : "?";
    }

    /**
     * Helper to draw centered text for paintComponent.
     *
     * @param g2        The Graphics2D context.
     * @param direction The direction corresponding to the equation text to draw.
     * @param targetX   The target X coordinate for the center of the string.
     * @param targetY   The target Y coordinate for the baseline of the string.
     */
    private void drawCenteredString(Graphics2D g2, String direction, int targetX, int targetY) {
        String text = getEqText(direction);
        FontMetrics metrics = g2.getFontMetrics();
        int textWidth = metrics.stringWidth(text);
        g2.drawString(text, targetX - (textWidth / 2), targetY);
    }

    /**
     * Draws the remaining cycle count left on the screen.
     *
     * @param g2 The Graphics2D context.
     */
    private void drawCycleLeft(Graphics2D g2) {
        simpleFontSetting(g2);

        String cycleText = "Cycles Left: " + (targetCycleCount - cycleCount);
        FontMetrics metrics = g2.getFontMetrics();
        int x = getWidth() - metrics.stringWidth(cycleText) - 40;
        int y = getHeight() - 50;

        g2.drawString(cycleText, x, y);
    }

    /**
     * Sets difficulty by adjusting max values for random generation.
     *
     * @param mode True for hard mode, false for easy mode.
     */
    public void setDifficulty(boolean mode) {
        this.hardMode = mode;
        if (hardMode) {
            max = 10;
        } else {
            max = 100;
        }
        resetGame();
    }

    /**
     * Resets the game state for a new session.
     */
    @Override
    public void resetGame() {
        cycleCount = 0;
        attemptsThisRound = 0;
        statusMessage = "";
        updateOptions();
        repaint();
    }

    /**
     * Increases the difficulty by expanding the maximum range by 10.
     */
    public void increaseDifficulty() {
        max += 10;
        difficultyIncreases++;
    }

    /**
     * Decreases the difficulty by shrinking the maximum range, ensuring it doesn't
     * go below 10.
     */
    public void decreaseDifficulty() {
        max = Math.max(10, max - 10);
        difficultyIncreases = Math.max(0, difficultyIncreases - 1);
    }

    /**
     * Resets the difficulty constraints to their default values.
     */
    public void resetDifficulty() {
        max = 100;
    }

    /**
     * Generates and assigns new equations for all directions.
     */
    public void updateOptions() {
        optionsMap.put("Up", generateNewEquation());
        optionsMap.put("Down", generateNewEquation());
        optionsMap.put("Left", generateNewEquation());
        optionsMap.put("Right", generateNewEquation());
        repaint();
    }

    /**
     * Generates a new equation for a single specified direction.
     *
     * @param direction The direction to refresh (e.g., "Up", "Left").
     */
    public void refreshDirection(String direction) {
        optionsMap.put(direction, generateNewEquation());
        repaint();
    }

    /**
     * Generates a new equation, ensuring it evaluates to a unique value
     * compared to existing options currently on the board.
     *
     * @return The newly generated, unique Equation.
     */
    public Equation generateNewEquation() {
        Equation newEq;
        boolean isDuplicate;
        do {
            Equation.EquationType type = hardMode ? Equation.EquationType.COMPLEX : Equation.EquationType.SIMPLE;
            newEq = new Equation(type, max);
            isDuplicate = false;
            for (Equation existingEq : optionsMap.values()) {
                if (existingEq != null && existingEq.evaluate() == newEq.evaluate()) {
                    isDuplicate = true;
                    break;
                }
            }
        } while (isDuplicate);
        return newEq;
    }

    /**
     * Handles user input, checks if the selected direction has the smallest value,
     * and updates the score and status message accordingly.
     *
     * @param input An Object representing the direction chosen (must be a String).
     */
    @Override
    public void handleInput(Object input) {
        String direction = (String) input;
        if (!optionsMap.containsKey(direction))
            return;

        if (isBoardStale()) {
            statusMessage = "Resetting the board";
            messageTimer.restart();
            updateOptions();
            return;
        }

        String targetDir = "Up";
        int lowestValue = Integer.MAX_VALUE;

        for (String dir : optionsMap.keySet()) {
            int value = optionsMap.get(dir).evaluate();
            if (value < lowestValue) {
                lowestValue = value;
                targetDir = dir;
            }
        }

        if (direction.equals(targetDir)) {
            double multiplier = hardMode ? HARD_MODE_SCALE : EASY_MODE_SCALE;
            session.processScore(attemptsThisRound, multiplier, 0);
            attemptsThisRound = 0;
            refreshDirection(direction);
            checkResetCondition(cycleCount++);
        } else {
            attemptsThisRound++;
            triesThisCycleCount++;
            statusMessage = "Try again";
            messageTimer.restart();
        }
        repaint();
    }

    /**
     * Checks if all equations are above a certain threshold or if the cycle count
     * has been met, triggering a milestone and board reset if necessary.
     *
     * @param cycleCount The current cycle count.
     */
    private void checkResetCondition(int cycleCount) {
        if (isBoardStale()) {
            int dynamicThreshold = (int) (max * 0.8); // attempt to keep threshold balanced, to be tested
            statusMessage = "You've gotten all options above " + dynamicThreshold
                    + ". Great work! Now randomizing options.";
            updateOptions();
        }
        if (cycleCount >= targetCycleCount) {
            statusMessage = "Correct! " + cycleCount + " cycle(s) completed. Now randomizing options.";
            dynamicDifficulty();
            triesThisCycleCount = 0;
            targetCycleCount = updateTargetCycleCount();
            double multiplier = hardMode ? HARD_MODE_SCALE : EASY_MODE_SCALE;
            session.processMilestone(multiplier, cycleCount);
            this.cycleCount = 0;
            updateOptions();
        }

    }

    /**
     * Checks if all equations on the board are above a certain threshold.
     *
     * @return true if the board is considered stale, false otherwise.
     */
    private boolean isBoardStale() {
        if (optionsMap.values().size() < 4)
            return false; // Don't check before board is fully initialized

        int dynamicThreshold = (int) (max * 0.8); // Scales threshold to always be 80% max value

        for (Equation eq : optionsMap.values()) {
            if (eq != null && eq.evaluate() < dynamicThreshold) {
                return false;
            }
        }
        return true;
    }

    /**
     * Adjusts the difficulty dynamically based on the player's performance:
     * - If player gets all answers correct for the target cycle count, increase
     * difficulty.
     * - If player struggles (e.g., reaches 3x the target cycle count in attempts),
     * decrease difficulty.
     */
    public void dynamicDifficulty() {
        if (triesThisCycleCount <= 0) {
            increaseDifficulty();
            double multiplier = hardMode ? HARD_MODE_SCALE : EASY_MODE_SCALE;
            session.processPerfectionBonus(difficultyIncreases, multiplier);
        } else if (triesThisCycleCount >= targetCycleCount * 2) {
            decreaseDifficulty();
        }
    }

    public int updateTargetCycleCount() {
        return random.nextInt(2) + 3;
    }
}