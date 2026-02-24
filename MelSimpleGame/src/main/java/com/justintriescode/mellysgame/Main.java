package com.justintriescode.mellysgame;

import com.justintriescode.mellysgame.ui.GameRunner;

public class Main {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            new GameRunner(); 
        });
    }
}