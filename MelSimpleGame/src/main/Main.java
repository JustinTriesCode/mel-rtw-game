package main;

import ui.GameRunner;

public class Main {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            new GameRunner(); 
        });
    }
}