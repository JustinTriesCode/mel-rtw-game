package ui;

import javax.swing.*;
import java.awt.*;

public class MenuPanel extends JPanel {
    public MenuPanel(GameRunner runner) {
        setBackground(new Color(210, 210, 200));
        setLayout(new GridBagLayout());
        
        JPanel buttonWrapper = new JPanel(new GridLayout(4, 1, 15, 15));
        buttonWrapper.setBackground(new Color(210, 210, 200));

        JLabel title = new JLabel("Melly's Minimalist Minigames", SwingConstants.CENTER);
        title.setForeground(new Color(90, 90, 90));
        title.setFont(new Font("Serif", Font.BOLD, 58));

        JButton game1Btn = new JButton("Alphabet Soup");
        JButton game2EasyBtn = new JButton("Equationista (Easy)");
        JButton game2HardBtn = new JButton("Equationista (Hard)");

        formatButton(game1Btn);
        formatButton(game2EasyBtn);
        formatButton(game2HardBtn);

        // Game 1 (Logic to be added later)
        game1Btn.addActionListener(e -> runner.showScreen("GAME1"));
        // Game 2
        game2EasyBtn.addActionListener(e -> runner.startGame(false));
        game2HardBtn.addActionListener(e -> runner.startGame(true));

        buttonWrapper.add(title);
        buttonWrapper.add(game1Btn);
        buttonWrapper.add(game2EasyBtn);
        buttonWrapper.add(game2HardBtn);

        this.add(buttonWrapper);
    }

    private void formatButton(JButton button) {
        button.setBackground(new Color(230, 235, 230));
        button.setForeground(new Color(100, 100, 85));
        button.setFont(new Font("Serif", Font.BOLD, 42));
        button.setPreferredSize(new Dimension(350, 60));

        button.setContentAreaFilled(false);
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setFocusPainted(false);

        button.setBorder(BorderFactory.createLineBorder(new Color(180, 185, 180), 2));
    }
}