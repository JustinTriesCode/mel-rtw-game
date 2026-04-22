package com.justintriescode.mellysgame.model;

/**
 * Interface for the mini-games to implement.
 */
public interface Playable {
    void resetGame(); // Reset scores/timers

    void handleInput(Object input); // Handle keys/buttons
}