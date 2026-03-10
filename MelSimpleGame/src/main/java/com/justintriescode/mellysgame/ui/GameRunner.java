package com.justintriescode.mellysgame.ui;

import javax.swing.*;

import com.justintriescode.mellysgame.game.Equationista;
import com.justintriescode.mellysgame.game.AlphabetSoup;
import com.justintriescode.mellysgame.game.BaseMiniGame;

import java.awt.*;

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
    public void startGame(String gameType, boolean hardMode, int durationSeconds) {

        BaseMiniGame newGame;
        if (gameType.equals("Equationista")) {
            // hardMode param determines number range (? 10 : 100) 
            newGame = new Equationista(this, hardMode, hardMode ? 10 : 100, durationSeconds);
        } else if (gameType.equals("AlphabetSoup")) {
            newGame = new AlphabetSoup(this, hardMode, durationSeconds);
        } else {
            // Placeholder for other games
            newGame = new AlphabetSoup(this, true, durationSeconds);
        }

        Component[] components = mainContainer.getComponents();
        for (Component comp : components) {
            if (!(comp instanceof MenuPanel)) {
                mainContainer.remove(comp);
            }
        }

        mainContainer.add(newGame, "GAME_SESSION");
        layout.show(mainContainer, "GAME_SESSION");

        mainContainer.revalidate();
        mainContainer.repaint();

        SwingUtilities.invokeLater(() -> {
            newGame.requestFocusInWindow();
        });
    }

    // EFFECTS: switches to the specified screen by name
    public void showScreen(String name) {
        layout.show(mainContainer, name);
    }
}
