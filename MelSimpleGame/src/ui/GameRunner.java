package ui;

import javax.swing.*;
import java.awt.*;

import game.Equationista;

public class GameRunner extends JFrame {
    private CardLayout layout = new CardLayout();
    private JPanel mainContainer = new JPanel(layout);

    public GameRunner() {
        setTitle("Melly's Minimalist Minigames");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);

        mainContainer.add(new MenuPanel(this), "MENU");
        add(mainContainer);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // EFFECTS: starts a new game session with the specified difficulty and
    // duration, and switches to the game screen
    public void startGame(boolean hardMode, int durationSeconds) {
        Equationista newGame = new Equationista(this, hardMode, hardMode ? 10 : 100, durationSeconds);
        mainContainer.add(newGame, "GAME2");
        layout.show(mainContainer, "GAME2");
        newGame.requestFocusInWindow();
    }

    // EFFECTS: switches to the specified screen by name
    public void showScreen(String name) {
        layout.show(mainContainer, name);
    }
}
