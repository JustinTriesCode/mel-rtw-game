package ui;

import javax.swing.*;
import java.awt.*;

public class GameRunner extends JFrame {
    private CardLayout layout = new CardLayout();
    private JPanel mainContainer = new JPanel(layout);

    public GameRunner() {
        setTitle("Janitaur Minigames");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);

        mainContainer.add(new MenuPanel(this), "MENU");
        // mainContainer.add(new KeySmasherPanel(), "GAME1");

        add(mainContainer);
        setVisible(true);
    }

    public void showScreen(String name) {
        layout.show(mainContainer, name);
    }
}
