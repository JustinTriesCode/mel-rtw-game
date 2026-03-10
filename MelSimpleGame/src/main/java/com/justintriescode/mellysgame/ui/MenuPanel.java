package com.justintriescode.mellysgame.ui;

import javax.swing.*;
import java.awt.*;

public class MenuPanel extends JPanel {
    private int selectedSeconds = 1 * 60; // Default to 1 minute

    public MenuPanel(GameRunner runner) {
        setBackground(new Color(210, 210, 200));
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JPanel buttonWrapper = new JPanel(new GridLayout(3, 1, 15, 15));
        buttonWrapper.setBackground(new Color(210, 210, 200));

        JLabel title = new JLabel("Melly's Minimalist Minigames", SwingConstants.CENTER);
        title.setForeground(new Color(90, 90, 90));
        title.setFont(new Font("Serif", Font.BOLD, 58));

        JButton game1EasyBtn = new JButton("Letter Soup");
        JButton game1HardBtn = new JButton("Alphabet Soup");
        JButton game2EasyBtn = new JButton("Numberista");
        JButton game2HardBtn = new JButton("Equationista");

        formatButton(game1EasyBtn);
        formatButton(game1HardBtn);
        formatButton(game2EasyBtn);
        formatButton(game2HardBtn);

        // Game 1
        game1EasyBtn.addActionListener(e -> runner.startGame("AlphabetSoup", false, selectedSeconds));
        game1HardBtn.addActionListener(e -> runner.startGame("AlphabetSoup", true, selectedSeconds));
        // Game 2
        game2EasyBtn.addActionListener(e -> runner.startGame("Equationista",false, selectedSeconds));
        game2HardBtn.addActionListener(e -> runner.startGame("Equationista",true, selectedSeconds));

        buttonWrapper.add(game1EasyBtn);
        buttonWrapper.add(game1HardBtn);
        buttonWrapper.add(game2EasyBtn);
        buttonWrapper.add(game2HardBtn);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // Span across both columns
        gbc.insets = new Insets(0, 0, 50, 0); // Big gap under title
        this.add(title, gbc);

        // Add Button List in the center-left
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(0, 0, 0, 0);
        this.add(buttonWrapper, gbc);

        // Add Toggle to the right of the buttons
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 50, 0, 0); // Gap between buttons and toggle
        this.add(createTimeToggle(), gbc);
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

    // EFFECTS: creates a panel with radio buttons to select game duration
    private JPanel createTimeToggle() {
        JPanel togglePanel = new JPanel();
        togglePanel.setLayout(new BoxLayout(togglePanel, BoxLayout.Y_AXIS));
        togglePanel.setBackground(new Color(210, 210, 200));

        JRadioButton oneMinBtn = new JRadioButton("1 Minute Session", true);
        JRadioButton twoMinBtn = new JRadioButton("2 Minute Session", false);

        ButtonGroup group = new ButtonGroup();
        group.add(oneMinBtn);
        group.add(twoMinBtn);

        oneMinBtn.addActionListener(e -> this.selectedSeconds = 1 * 60);
        twoMinBtn.addActionListener(e -> this.selectedSeconds = 2 * 60);

        JLabel label = new JLabel("Timer Settings:");
        label.setFont(new Font("Serif", Font.ITALIC, 16));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        togglePanel.add(label);
        togglePanel.add(Box.createVerticalStrut(10));

        for (JRadioButton btn : new JRadioButton[] { oneMinBtn, twoMinBtn }) {
            btn.setFont(new Font("Serif", Font.PLAIN, 18));
            btn.setForeground(new Color(90, 90, 90));
            btn.setBackground(togglePanel.getBackground());
            btn.setFocusPainted(false);
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            togglePanel.add(btn);
        }

        return togglePanel;
    }
}