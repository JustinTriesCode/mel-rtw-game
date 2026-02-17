package ui;

import javax.swing.*;
import java.awt.*;

public class MenuPanel extends JPanel {
    public MenuPanel(GameRunner runner) {
        setLayout(new GridLayout(4, 1, 10, 10));
        
        JLabel title = new JLabel("Return To Work Test", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        
        JButton game1Btn = new JButton("Alphabet Soup");
        JButton game2EasyBtn = new JButton("Equationista (Easy)");
        JButton game2HardBtn = new JButton("Equationista (Hard)");

        // Game 1 (Logic to be added later)
        game1Btn.addActionListener(e -> runner.showScreen("GAME1"));

        // Game 2 - Easy Mode
        game2EasyBtn.addActionListener(e -> runner.startGame(false));

        game2HardBtn.addActionListener(e -> runner.startGame(true));

        // tell GameRunner to switch
        game1Btn.addActionListener(e -> runner.showScreen("GAME1"));
        game2EasyBtn.addActionListener(e -> runner.showScreen("GAME2"));
        game2HardBtn.addActionListener(e -> runner.showScreen("GAME2"));
        add(title);
        add(game1Btn);
        add(game2EasyBtn);
        add(game2HardBtn);
    }
}