package game;

import model.Playable;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import model.Equation;
import ui.ResourceLoader;
import ui.GameRunner;

public class Equationista extends JPanel implements Playable {
    private boolean hardMode = false;
    private HashMap<String, Equation> optionsMap = new HashMap<>();
    private int max = 100;
    private BufferedImage arrowImage;
    private GameRunner runner;
    private int score = 0;
    private int attemptsThisRound = 0;
    private String statusMessage = "";
    private Timer messageTimer;

    public Equationista(GameRunner runner, boolean hardMode, int maxRange) {
        arrowImage = ResourceLoader.loadImage("arrowKeys.png");
        setBackground(new Color(45, 45, 45));
        this.runner = runner;
        this.hardMode = hardMode;
        this.max = maxRange;
        this.setFocusable(true);

        // Initialize the HashMap with arrow directions and corresponding equations
        updateOptions();
        // Layout buttons to represent arrow directions (Up, Down, Left, Right)
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
                if (code == java.awt.event.KeyEvent.VK_ESCAPE) {
                    runner.showScreen("MENU");
                    return;
                }
            }
        });
        resizeImg(1);
    }

    public void resizeImg(double scale) {
        BufferedImage original = ResourceLoader.loadImage("arrowKeys.png");
        int w = (int) (original.getWidth() * scale);
        int h = (int) (original.getHeight() * scale);
        if (original != null) {
            Image scaled = original.getScaledInstance(w, h, Image.SCALE_SMOOTH);

            this.arrowImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = this.arrowImage.createGraphics();
            g2d.drawImage(scaled, 0, 0, null);
            g2d.dispose();
        }
    }

    // EFFECTS: paints the game screen with the arrow image and equations
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        drawGameAssets(g2);
        drawScore(g2);
        drawStatusMessage(g2);
    }

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
        g2.drawString("Select the smallest value!", 20, 30);
        FontMetrics fontMetrics = g2.getFontMetrics();
        int fontHeight = fontMetrics.getHeight();
        int padding = fontHeight /2;

        // Draw the Equations
        drawCenteredString(g2, "Up", centerX, y - padding);
        drawCenteredString(g2, "Down", centerX, y + imgH + fontHeight);
        drawCenteredString(g2, "Left", x - padding*3, centerY + (imgH / 3));
        drawCenteredString(g2, "Right", x + imgW + padding*3, centerY + (imgH / 3));
    }

    // EFFECTS: helper to clean text for paintComponent
    private String getEqText(String direction) {
        Equation eq = optionsMap.get(direction);
        return (eq != null) ? eq.toString() : "?";
    }

    // EFFECTS: helper to draw centered text for paintComponent
    private void drawCenteredString(Graphics2D g2, String direction, int targetX, int targetY) {
        String text = getEqText(direction);
        FontMetrics metrics = g2.getFontMetrics();
        int textWidth = metrics.stringWidth(text);
        g2.drawString(text, targetX - (textWidth / 2), targetY);
    }

    // EFFECTS: draws the score on the screen
    private void drawScore(Graphics2D g2) {
        g2.setFont(new Font("Serif", Font.PLAIN, 28));
        g2.setColor(new Color(100, 100, 90));

        String scoreText = "Score: " + score;
        FontMetrics metrics = g2.getFontMetrics();
        int x = getWidth() - metrics.stringWidth(scoreText) - 40;
        int y = 50;

        g2.drawString(scoreText, x, y);
    }

    // EFFECTS: draws the status message at the bottom of the screen
    private void drawStatusMessage(Graphics2D g2) {
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

    // EFFECTS: sets difficulty by adjusting max values for random generation
    public void setDifficulty(boolean mode) {
        this.hardMode = mode;
        if (hardMode) {
            max = 10;
        } else {
            max = 100;
        }
        resetGame();
    }

    @Override
    public void resetGame() {
        updateOptions();
        repaint();
    }

    // EFFECTS: increases difficulty by 10
    public void increaseDifficulty() {
        max += 10;
    }

    // EFFECTS: decreases difficulty, ensuring it doesn't go below 10
    public void decreaseDifficulty() {
        max = Math.max(10, max - 10);
    }

    // EFFECTS: resets difficulty to default
    public void resetDifficulty() {
        max = 100;
    }

    // EFFECTS: generates new equations for all directions
    public void updateOptions() {
        optionsMap.put("Up", generateNewEquation());
        optionsMap.put("Down", generateNewEquation());
        optionsMap.put("Left", generateNewEquation());
        optionsMap.put("Right", generateNewEquation());
        repaint();
    }

    // EFFECTS: generates a new equation for a single direction
    public void refreshDirection(String direction) {
        optionsMap.put(direction, generateNewEquation());
        repaint();
    }

    // EFFECTS: generates a new equation, ensuring it's not a duplicate of existing
    // options
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

    // EFFECTS: handles user input, checks if the selected direction is correct, updates score and status message
    @Override
    public void handleInput(Object input) {
        String direction = (String) input;
        if (!optionsMap.containsKey(direction))
            return;

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
            int multiplier = hardMode ? 5 : 1;
            int points = Math.max(-10, (10 * multiplier) - (attemptsThisRound * 5));
            score += points;
            statusMessage = "Correct! " + points + " points.";
            attemptsThisRound = 0;
            refreshDirection(direction);
            checkResetCondition();
        } else {
            attemptsThisRound++;
            statusMessage = "Try again.";
        }

        if (messageTimer != null)
            messageTimer.stop();
        messageTimer = new Timer(1500, e -> {
            statusMessage = "";
            repaint();
        });
        messageTimer.setRepeats(false);
        messageTimer.start();

        repaint();
    }

    // EFFECTRS: checks if all equations are above a certain threshold to trigger a
    // reset
    private void checkResetCondition() {
        boolean allHigh = true;
        int threshold = 90;

        for (Equation eq : optionsMap.values()) {
            if (eq.evaluate() < threshold) {
                allHigh = false;
                break;
            }
        }
        if (allHigh) {
            statusMessage = "You've gotten all options above " + threshold + ". Great work! Now randomizing options.";
            // update with win conditions later...
            updateOptions();
        }
    }
}