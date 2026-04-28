package com.justintriescode.mellysgame.ui;

import javax.swing.*;
import java.awt.*;

/**
 * The main menu screen of the application.
 * Displays game selection buttons and allows the player to configure
 * session settings such as game duration.
 */
public class MenuPanel extends JPanel {
    private int selectedSeconds = 1 * 60; // Defaults to 1 minute

    /**
     * Constructs the MenuPanel, initializing the layout, game buttons,
     * and configuration toggles.
     *
     * @param runner The main GameRunner instance used for screen transitions and
     *               starting games.
     */
    public MenuPanel(GameRunner runner) {
        setBackground(UIStyleUtils.BG_COLOR);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JPanel gamesGrid = new JPanel(new GridLayout(4, 1, 15, 15));
        gamesGrid.setBackground(UIStyleUtils.BG_COLOR);

        JLabel title = new JLabel("Melly's Minimalist Minigames", SwingConstants.CENTER);
        title.setForeground(UIStyleUtils.TEXT_DARK);
        title.setFont(new Font("Serif", Font.BOLD, 58));

        JButton game1EasyBtn = new JButton("Letter Soup");
        JButton game1HardBtn = new JButton("Alphabet Soup");
        JButton game2EasyBtn = new JButton("Numberista");
        JButton game2HardBtn = new JButton("Equationista");
        JButton statsBtn = new JButton("View Player Stats");

        UIStyleUtils.formatButton(game1EasyBtn, UIStyleUtils.MENU_FONT_SIZE);
        UIStyleUtils.formatButton(game1HardBtn, UIStyleUtils.MENU_FONT_SIZE);
        UIStyleUtils.formatButton(game2EasyBtn, UIStyleUtils.MENU_FONT_SIZE);
        UIStyleUtils.formatButton(game2HardBtn, UIStyleUtils.MENU_FONT_SIZE);
        UIStyleUtils.formatButton(statsBtn, 28);

        // Game 1: Letter Games
        game1EasyBtn.addActionListener(e -> runner.startGame("AlphabetSoup", false, selectedSeconds));
        game1HardBtn.addActionListener(e -> runner.startGame("AlphabetSoup", true, selectedSeconds));
        // Game 2: Number Games
        game2EasyBtn.addActionListener(e -> runner.startGame("Equationista", false, selectedSeconds));
        game2HardBtn.addActionListener(e -> runner.startGame("Equationista", true, selectedSeconds));
        // Stats
        statsBtn.addActionListener(e -> showStatsDialog(runner));

        gamesGrid.add(game1EasyBtn);
        gamesGrid.add(game1HardBtn);
        gamesGrid.add(game2EasyBtn);
        gamesGrid.add(game2HardBtn);

        JPanel buttonWrapper = new JPanel(new BorderLayout(0, 40));
        buttonWrapper.setBackground(UIStyleUtils.BG_COLOR);
        buttonWrapper.add(gamesGrid, BorderLayout.CENTER);
        buttonWrapper.add(statsBtn, BorderLayout.SOUTH);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // Span across both columns
        gbc.insets = new Insets(0, 0, 50, 0);
        this.add(title, gbc);

        // Add Button List in the center-left
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(0, 0, 0, 0);
        this.add(buttonWrapper, gbc);

        // Container for the timer toggle and Quit button
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(UIStyleUtils.BG_COLOR);

        JPanel topRightPanel = new JPanel();
        topRightPanel.setLayout(new BoxLayout(topRightPanel, BoxLayout.Y_AXIS));
        topRightPanel.setBackground(UIStyleUtils.BG_COLOR);

        Image appIcon = ResourceLoader.loadImage("app_icon.png");
        if (appIcon != null) {
            JPanel iconPanel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

                    int size = 150;
                    int x = (getWidth() - size) / 2;
                    int y = (getHeight() - size) / 2;
                    g2.drawImage(appIcon, x, y, size, size, this);
                }
            };
            iconPanel.setOpaque(false);
            iconPanel.setPreferredSize(new Dimension(150, 150));
            iconPanel.setMaximumSize(new Dimension(150, 150));
            iconPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
            topRightPanel.add(iconPanel);
            topRightPanel.add(Box.createVerticalStrut(30));
        }

        topRightPanel.add(createTimeToggle());
        rightPanel.add(topRightPanel, BorderLayout.NORTH);

        JButton quitBtn = new JButton("Quit Game");
        UIStyleUtils.formatButton(quitBtn, 24);
        quitBtn.addActionListener(e -> System.exit(0));

        JPanel quitWrapper = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        quitWrapper.setBackground(UIStyleUtils.BG_COLOR);
        quitWrapper.add(quitBtn);
        rightPanel.add(quitWrapper, BorderLayout.SOUTH);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.insets = new Insets(0, 50, 0, 0);
        this.add(rightPanel, gbc);
    }

    /**
     * Opens a dialog displaying the player's lifetime statistics.
     *
     * @param runner The GameRunner instance holding the player profile.
     */
    private void showStatsDialog(GameRunner runner) {
        StatsPanel statsPanel = new StatsPanel(runner);
        runner.showGenericDialog("Player Statistics", statsPanel, () -> {
        });
    }

    /**
     * Creates a panel containing radio buttons to select the game duration.
     *
     * @return A JPanel configured with timer selection options.
     */
    private JPanel createTimeToggle() {
        JPanel togglePanel = new JPanel();
        togglePanel.setLayout(new BoxLayout(togglePanel, BoxLayout.Y_AXIS));
        togglePanel.setBackground(UIStyleUtils.BG_COLOR);

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
            formatRadioButton(btn);
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            togglePanel.add(btn);
        }

        return togglePanel;
    }

    /**
     * Helper method to format radio buttons consistently with the application's
     * theme.
     *
     * @param btn The JRadioButton to format.
     */
    private void formatRadioButton(JRadioButton btn) {
        btn.setFont(new Font("Serif", Font.PLAIN, 18));
        btn.setForeground(UIStyleUtils.TEXT_DARK);
        btn.setBackground(UIStyleUtils.BG_COLOR);
        btn.setFocusPainted(false);
    }
}