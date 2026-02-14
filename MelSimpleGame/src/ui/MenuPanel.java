package ui;

import javax.swing.*;
import java.awt.*;

public class MenuPanel extends JPanel {
    public MenuPanel(GameRunner runner) {
        setLayout(new GridLayout(3, 1, 10, 10));
        
        JLabel title = new JLabel("Return To Work Test", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        
        JButton game1Btn = new JButton("Start Key Smasher");
        JButton game2Btn = new JButton("Start Number Hunter");

        // tell GameRunner to switch
        game1Btn.addActionListener(e -> runner.showScreen("GAME1"));
        game2Btn.addActionListener(e -> runner.showScreen("GAME2"));

        add(title);
        add(game1Btn);
        add(game2Btn);
    }
}
