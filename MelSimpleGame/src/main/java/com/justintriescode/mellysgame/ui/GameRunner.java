package com.justintriescode.mellysgame.ui;

import javax.swing.*;

import com.justintriescode.mellysgame.game.Equationista;
import com.justintriescode.mellysgame.data.DataManager;
import com.justintriescode.mellysgame.game.AlphabetSoup;
import com.justintriescode.mellysgame.game.BaseMiniGame;
import com.justintriescode.mellysgame.game.PlayerProfile;

import java.awt.*;
import java.io.IOException;

public class GameRunner extends JFrame {
    private CardLayout layout = new CardLayout();
    private JPanel mainContainer = new JPanel(layout);
    private String currentSymptomSeverity;
    private PlayerProfile playerProfile;

    public GameRunner() {
        setTitle("Melly's Minimalist Minigames");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        this.playerProfile = DataManager.load();

        mainContainer.add(new MenuPanel(this), "MENU");
        add(mainContainer);
        setLocationRelativeTo(null);
        setVisible(true);
        // trigger the symptom popup right after the UI is visible
        SwingUtilities.invokeLater(() -> promptForSymptom());
    }

    public PlayerProfile getPlayerProfile() {
        return playerProfile;
    }

    private void promptForSymptom() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);

        ButtonGroup group = new ButtonGroup();
        String[] options = { "Mild", "Moderate", "Severe" };
        JRadioButton[] buttons = new JRadioButton[options.length];

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
                // Add error handling or logging here if needed
            }
        });
    }

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

    // Getter so your GameSession or MiniGames can grab the value for scoring/stats
    public String getCurrentSymptomSeverity() {
        return currentSymptomSeverity;
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

    public void handleSessionEnd(String gameName, boolean isHard, int score, int streak) {
        playerProfile.recordSessionContext(
                gameName,
                this.currentSymptomSeverity,
                isHard,
                score);
        playerProfile.recordSessionContext(gameName, currentSymptomSeverity, isHard, score);
        try {
            DataManager.save(playerProfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        showGameOver(score, streak);
    }
}
