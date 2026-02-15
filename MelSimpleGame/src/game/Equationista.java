package game;

import model.Playable;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.Buffer;
import java.util.HashMap;

import model.Equation;
import ui.ResourceLoader;

public class Equationista extends JPanel implements Playable {
    private boolean hardMode = false;
    //private Equation[] options = {null, null, null, null}; // Placeholders for numbers/equations
    private HashMap<String, Equation> optionsMap = new HashMap<>();
    private int max = 100; // default max
    private BufferedImage arrowImage;
    private int width = 800;
    private int height = 600;

    public Equationista() {
        arrowImage = ResourceLoader.loadImage("arrowKeys.png");
        setBackground(new Color(45, 45, 45));
        // Initialize the HashMap with arrow directions and corresponding equations
        optionsMap.put("Up", null);
        optionsMap.put("Down", null);
        optionsMap.put("Left", null);
        optionsMap.put("Right", null);
        // TODO: Layout buttons to represent arrow directions (Up, Down, Left, Right)
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.WHITE);
        g.drawImage(arrowImage, height/2, width/2, null);
        g.drawString("Game 2: Select the correct value!", 20, 30);
        
        // draw the equation strings for each option
        g.drawString(optionsMap.get("Up") != null ? optionsMap.get("Up").toString() : "?", height/2 + 20, width/2 - 40);
        g.drawString(optionsMap.get("Down") != null ? optionsMap.get("Down").toString() : "?", height/2 + 20, width/2 + 40);
        g.drawString(optionsMap.get("Left") != null ? optionsMap.get("Left").toString() : "?", height/2 - 40, width/2);
        g.drawString(optionsMap.get("Right") != null ? optionsMap.get("Right").toString() : "?", height/2 + 80, width/2);
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
        // TODO: Use the Equation class to generate new numbers/math
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

    public void updateOptions() {
        if (!hardMode) {
            optionsMap.put("Up", new Equation(Equation.EquationType.SIMPLE, max));
            optionsMap.put("Down", new Equation(Equation.EquationType.SIMPLE, max));
            optionsMap.put("Left", new Equation(Equation.EquationType.SIMPLE, max));
            optionsMap.put("Right", new Equation(Equation.EquationType.SIMPLE, max));
        } else {
            optionsMap.put("Up", new Equation(Equation.EquationType.COMPLEX, max));
            optionsMap.put("Down", new Equation(Equation.EquationType.COMPLEX, max));
            optionsMap.put("Left", new Equation(Equation.EquationType.COMPLEX, max));
            optionsMap.put("Right", new Equation(Equation.EquationType.COMPLEX, max));
        }
        repaint();
    }

    @Override 
    public void handleInput(Object input) {
        // TODO: Check if the clicked button matches the target value
    }
}