package model;

public interface Playable {
    void resetGame();      // Reset scores/timers
    void handleInput(Object input); // Handle keys/buttons
}