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
        setSize(800, 600);

        mainContainer.add(new MenuPanel(this), "MENU");
        // mainContainer.add(new KeySmasherPanel(), "GAME1");

        add(mainContainer);
        setLocationRelativeTo(null);
        setVisible(true);

        // Equationista gameTwo = new Equationista(this, false, 100);
        // mainContainer.add(gameTwo, "GAME2");
    }

    public void startGame(boolean hardMode) {
        Equationista newGame = new Equationista(this, hardMode, hardMode ? 10 : 100);
        mainContainer.add(newGame, "GAME2");
        layout.show(mainContainer, "GAME2");
        newGame.requestFocusInWindow();
    }

    public void showScreen(String name) {
        layout.show(mainContainer, name);
    }
}
