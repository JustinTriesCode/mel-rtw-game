package com.justintriescode.mellysgame.events;

/**
 * A generic interface for observing updates from an Observable object.
 *
 * @param <T> The type of data being passed during the update.
 */
public interface Observer<T> {
    void onUpdate(T data);
}
