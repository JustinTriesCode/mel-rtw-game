package game;

import model.Playable;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
//import java.nio.Buffer;
import java.util.HashMap;

import model.Equation;
import ui.ResourceLoader;
import ui.GameRunner;

public class Equationista extends JPanel implements Playable {
    private boolean hardMode = false;
    private HashMap<String, Equation> optionsMap = new HashMap<>();
    private int max = 100; // default max
    private BufferedImage arrowImage;
    private GameRunner runner;

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
                if (code == java.awt.event.KeyEvent.VK_UP)
                    handleInput("Up");
                if (code == java.awt.event.KeyEvent.VK_DOWN)
                    handleInput("Down");
                if (code == java.awt.event.KeyEvent.VK_LEFT)
                    handleInput("Left");
                if (code == java.awt.event.KeyEvent.VK_RIGHT)
                    handleInput("Right");
                if (code == java.awt.event.KeyEvent.VK_ESCAPE) {
                    runner.showScreen("MENU");
                    return;
                }
            }
        });
        resizeImg(1.5);
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

        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        int imgW = arrowImage.getWidth();
        int imgH = arrowImage.getHeight();
        int x = centerX - (imgW / 2);
        int y = centerY - (imgH / 2);

        // Draw the image at the center
        g2.drawImage(arrowImage, x, y, null);

        // Font and color for text
        g2.setColor(new Color(251, 250, 245));
        g2.setFont(new Font("Serif", Font.BOLD, 24));
        g2.drawString("Equationista: Select the smallest value!", 20, 30);

        // Draw the Equations
        g2.drawString(getEqText("Up"), centerX - 15, y - 10);
        g2.drawString(getEqText("Down"), centerX - 15, y + imgH + 25);
        g2.drawString(getEqText("Left"), x - 10, centerY + (imgH / 4));
        g2.drawString(getEqText("Right"), x + imgW, centerY + (imgH / 4));
    }

    // EFFECTS: helper to clean text for paintComponent
    private String getEqText(String direction) {
        Equation eq = optionsMap.get(direction);
        return (eq != null) ? eq.toString() : "?";
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
            System.out.println("Correct!");
            refreshDirection(direction);
        } else {
            System.out.println("Wrong! Try again.");
        }
    }
}