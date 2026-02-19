package main.java.com.justintriescode.mellysgame.model;

public interface Playable {
    void resetGame();      // Reset scores/timers
    void handleInput(Object input); // Handle keys/buttons
}