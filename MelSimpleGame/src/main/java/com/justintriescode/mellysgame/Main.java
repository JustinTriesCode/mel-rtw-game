package main.java.com.justintriescode.mellysgame;

import main.java.com.justintriescode.mellysgame.ui.GameRunner;

public class Main {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            new GameRunner(); 
        });
    }
}