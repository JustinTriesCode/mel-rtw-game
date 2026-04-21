package com.justintriescode.mellysgame.events;

import java.util.ArrayList;
import java.util.List;

/**
 * Singleton class that maintains a log of events that occur during the game
 * session.
 * It allows adding new events and retrieving the entire log as a formatted
 * string.
 */
public class EventLog {
    private static EventLog instance;
    private List<Event> events;

    /**
     * Private constructor to prevent instantiation from outside the class.
     */
    private EventLog() {
        events = new ArrayList<>();
    }

    /**
     * Returns the singleton instance of the EventLog. If it does not exist, it
     * creates one.
     *
     * @return the singleton instance of EventLog
     */
    public static EventLog getInstance() {
        if (instance == null) {
            instance = new EventLog();
        }
        return instance;
    }

    /**
     * Adds a new event to the log.
     *
     * @param event the event to be added
     */
    public void addEvent(Event event) {
        events.add(event);
    }

    /**
     * Retrieves the entire event log as a formatted string.
     *
     * @return a string representation of the event log
     */
    public String getLog() {
        StringBuilder log = new StringBuilder();
        for (Event event : events) {
            log.append("[").append(event.getTimestamp()).append("] ").append(event.getDescription()).append("\n");
        }
        return log.toString();
    }

}
