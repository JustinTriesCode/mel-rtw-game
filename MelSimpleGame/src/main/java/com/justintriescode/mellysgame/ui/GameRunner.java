package com.justintriescode.mellysgame.ui;

import javax.swing.*;

import com.justintriescode.mellysgame.game.Equationista;
import com.justintriescode.mellysgame.data.DataManager;
import com.justintriescode.mellysgame.game.AlphabetSoup;
import com.justintriescode.mellysgame.game.BaseMiniGame;
import com.justintriescode.mellysgame.game.PlayerProfile;
import com.justintriescode.mellysgame.events.Event;
import com.justintriescode.mellysgame.events.EventLog;

import java.awt.*;
import java.io.IOException;

/**
 * The UI container for the application.
 * Manages screen transitions, dialogs, and the lifecycle of game sessions.
 */
public class GameRunner extends JFrame {
    private static final int WIDTH = 1200;
    private static final int HEIGHT = 800;
    private static final int SPLASH_DURATION = 3600;
    private CardLayout layout = new CardLayout();
    private JPanel mainContainer = new JPanel(layout);
    private String currentSymptomSeverity = "None"; // Default to "None"
    private PlayerProfile playerProfile;

    /**
     * Initializes the GameRunner, sets up the main window, loads the player
     * profile,
     * and prompts the user for their current symptom severity.
     */
    public GameRunner() {
        setTitle("Melly's Minimalist Minigames");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);

        getRootPane().putClientProperty("apple.awt.fullscreenable", true);
        this.playerProfile = DataManager.load();

        // Loads the icon to use for the app (reminder: loads from images folder)
        Image appIcon = com.justintriescode.mellysgame.ui.ResourceLoader.loadImage("app_icon.png");
        if (appIcon != null) {
            setIconImage(appIcon);
            try { // for macOC testing
                Class<?> taskbarClass = Class.forName("java.awt.Taskbar");
                Object taskbar = taskbarClass.getMethod("getTaskbar").invoke(null);
                taskbarClass.getMethod("setIconImage", java.awt.Image.class).invoke(taskbar, appIcon);
            } catch (Exception e) {
                EventLog.getInstance()
                        .addEvent(new Event("Failed to set taskbar icon (probably running on non-MacOS)", e));
            }
        }

        // Register GameRunner to listen for High Score events from the PlayerProfile
        this.playerProfile.addObserver(profile -> {
            JOptionPane.showMessageDialog(this,
                    "Amazing! You set a new High Score of " + profile.getTopScore() + "!",
                    "New High Score!",
                    JOptionPane.PLAIN_MESSAGE);
        });

        mainContainer.add(new MenuPanel(this), "MENU");
        add(mainContainer);
        setLocationRelativeTo(null);

        // Splash Screen Logic (uses app icon)
        if (appIcon != null) {
            JWindow splash = new JWindow();
            splash.setBackground(new Color(0, 0, 0, 0)); // transparent
            JLabel splashImage = new JLabel(new ImageIcon(appIcon.getScaledInstance(256, 256, Image.SCALE_SMOOTH)));
            splashImage.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
            splash.getContentPane().add(splashImage, BorderLayout.CENTER);
            splash.pack();
            splash.setLocationRelativeTo(null);
            splash.setVisible(true);

            Timer splashTimer = new Timer(SPLASH_DURATION, e -> {
                splash.dispose();
                setVisible(true);
                SwingUtilities.invokeLater(() -> promptForSymptom());
            });
            splashTimer.setRepeats(false);
            splashTimer.start();
        } else {
            setVisible(true);
            SwingUtilities.invokeLater(() -> promptForSymptom());
        }
    }

    /**
     * Gets the currently loaded player profile.
     *
     * @return The active {@link PlayerProfile}.
     */
    public PlayerProfile getPlayerProfile() {
        return playerProfile;
    }

    /**
     * Prompts the user to select their current symptom severity before playing.
     * Saves the selection to the player profile.
     */
    private void promptForSymptom() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);

        ButtonGroup group = new ButtonGroup();
        String[] options = { "None", "Mild", "Moderate", "Severe" };
        JRadioButton[] buttons = new JRadioButton[options.length];

        // creates 1 button for each string in options array.
        for (int i = 0; i < options.length; i++) {
            buttons[i] = new JRadioButton(options[i]);
            buttons[i].setFont(new Font("Serif", Font.PLAIN, 18));
            buttons[i].setOpaque(false);
            if (i == 0)
                buttons[i].setSelected(true);
            group.add(buttons[i]);
            panel.add(buttons[i]);
        }

        showGenericDialog("How are your symptoms today?", panel, () -> {
            for (JRadioButton b : buttons) {
                if (b.isSelected()) {
                    this.currentSymptomSeverity = b.getText();
                    break;
                }
            }
            this.playerProfile.setLastRecordedSeverity(this.currentSymptomSeverity);
            try {
                com.justintriescode.mellysgame.data.DataManager.save(this.playerProfile);
            } catch (IOException e) {
                EventLog.getInstance().addEvent(new Event("Failed to save player profile after symptom selection", e));
            }
        });
    }

    /**
     * Displays a customized, undecorated modal dialog with the given content.
     *
     * @param title     The title to display at the top of the dialog.
     * @param content   The JPanel containing the custom content for the dialog
     *                  body.
     * @param onConfirm A Runnable callback to execute when the "Confirm" button is
     *                  clicked.
     */
    public void showGenericDialog(String title, JPanel content, Runnable onConfirm) {
        JDialog dialog = new JDialog(this, true);
        dialog.setUndecorated(true);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 20));
        UIStyleUtils.stylePopupPanel(mainPanel);

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.ITALIC, 28));
        titleLabel.setForeground(UIStyleUtils.TEXT_DARK);
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        content.setOpaque(false);
        mainPanel.add(content, BorderLayout.CENTER);

        JButton confirmBtn = new JButton("Confirm");
        UIStyleUtils.formatButton(confirmBtn, 20);
        confirmBtn.addActionListener(e -> {
            onConfirm.run();
            dialog.dispose();
        });
        mainPanel.add(confirmBtn, BorderLayout.SOUTH);

        dialog.add(mainPanel);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    /**
     * Gets the currently selected symptom severity.
     * Useful for scoring or statistics tracking in game sessions.
     *
     * @return The current symptom severity as a string.
     */
    public String getCurrentSymptomSeverity() {
        return currentSymptomSeverity;
    }

    /**
     * Starts a new game session with the specified difficulty and duration,
     * and switches the view to the new game screen.
     *
     * @param gameType        The name of the mini-game to start (e.g.,
     *                        "Equationista").
     * @param hardMode        True to enable hard mode, false for easy mode.
     * @param durationSeconds The duration of the game session in seconds.
     */
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

    /**
     * Switches the main container to display the specified screen.
     *
     * @param name The name identifier of the screen to show (e.g., "MENU",
     *             "GAME_SESSION").
     */
    public void showScreen(String name) {
        layout.show(mainContainer, name);
    }

    /**
     * Displays the game over dialog with the final score and streak,
     * then returns to the main menu upon confirmation.
     *
     * @param score  The final score achieved in the session.
     * @param streak The longest streak achieved in the session.
     */
    public void showGameOver(int score, int streak) {
        JPanel statsPanel = new JPanel(new GridLayout(2, 1));
        statsPanel.setOpaque(false);

        JLabel sLabel = new JLabel("Final Score: " + score);
        JLabel stLabel = new JLabel("Longest Streak: " + streak);
        sLabel.setForeground(UIStyleUtils.TEXT_DARK);
        stLabel.setForeground(UIStyleUtils.TEXT_DARK);

        statsPanel.add(sLabel);
        statsPanel.add(stLabel);

        showGenericDialog("Session Complete", statsPanel, () -> {
            showScreen("MENU");
        });
    }

    /**
     * Handles the end of a game session by recording the context to the player
     * profile,
     * saving the data, and triggering the game over screen.
     *
     * @param gameName The name of the mini-game that just ended.
     * @param isHard   True if the game was played on hard mode, false otherwise.
     * @param score    The final score achieved in the session.
     * @param streak   The longest streak achieved in the session.
     */
    public void handleSessionEnd(String gameName, boolean isHard, int score, int streak) {
        playerProfile.recordSessionContext(
                gameName,
                this.currentSymptomSeverity,
                isHard,
                score);
        try {
            DataManager.save(playerProfile);
        } catch (IOException e) {
            EventLog.getInstance().addEvent(new Event("Failed to save player profile after session end", e));
        }
        showGameOver(score, streak);
    }

    /**
     * Toggles the game window between windowed and maximized full screen.
     */
    public void toggleFullScreen() {
        if (getExtendedState() == JFrame.MAXIMIZED_BOTH) {
            setExtendedState(JFrame.NORMAL);
        } else {
            setExtendedState(JFrame.MAXIMIZED_BOTH);
        }
    }
}
