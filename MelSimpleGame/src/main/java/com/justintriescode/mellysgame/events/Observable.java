package com.justintriescode.mellysgame.events;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract class that provides generic observable functionality.
 */
public abstract class Observable<T> {
    private transient final List<Observer<T>> observers = new ArrayList<>();

    protected void notifyObservers(T data) {
        for (Observer<T> observer : new ArrayList<>(observers)) {
            observer.onUpdate(data);
        }
    }

    public void addObserver(Observer<T> obs) {
        if (obs != null && !observers.contains(obs)) {
            observers.add(obs);
        }
    }

    public void removeObserver(Observer<T> obs) {
        observers.remove(obs);
    }
}
