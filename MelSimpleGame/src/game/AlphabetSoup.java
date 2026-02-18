package game;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

import model.Playable;

public class AlphabetSoup extends JPanel implements Playable {

    public AlphabetSoup() {
        setBackground(Color.DARK_GRAY);
        // TODO: Add KeyListener here
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.WHITE);
        g.drawString("Game 1: Smash the Key!", 50, 50);
        // TODO: g.drawImage(...)
    }

    @Override
    public void resetGame() {
        /* TODO */ }

    @Override
    public void handleInput(Object input) {
        /* TODO */ }
}
