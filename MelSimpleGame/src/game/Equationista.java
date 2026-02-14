package game;

import model.Playable;
import javax.swing.*;
import java.awt.*;

public class Equationista extends JPanel implements Playable {
    private boolean hardMode = false;
    private String[] options = {"?", "?", "?", "?"}; // Placeholders for numbers/equations

    public Equationista() {
        setBackground(new Color(45, 45, 45));
        // TODO: Layout buttons to represent arrow directions (Up, Down, Left, Right)
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.WHITE);
        g.drawString("Game 2: Select the correct value!", 20, 30);
        // TODO: Draw arrows and the current strings from the 'options' array
    }

    public void setHardMode(boolean mode) {
        this.hardMode = mode;
        resetGame();
    }

    @Override 
    public void resetGame() { 
        // TODO: Use the Equation class to generate new numbers/math
        repaint();
    }

    @Override 
    public void handleInput(Object input) {
        // TODO: Check if the clicked button matches the target value
    }
}