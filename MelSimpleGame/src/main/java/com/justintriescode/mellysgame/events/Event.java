package com.justintriescode.mellysgame.events;

/**
 * Represents an even that occurs in the game.
 */
public class Event {
    private String description;
    private long timestamp;
    private Exception exception;

    /**
     * Gets the description of the event.
     *
     * @return the event description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets the timestamp of when the event occurred.
     *
     * @return the event timestamp
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * Gets the exception associated with the event, if any.
     *
     * @return the event exception or null if none
     */
    public Exception getException() {
        return exception;
    }

    /**
     * Constructs a new Event with the given description.
     *
     * @param description the event description
     */
    public Event(String description) {
        this.description = description;
        this.timestamp = System.currentTimeMillis();
        this.exception = null;
    }

    /**
     * Constructs a new Event with the given description and exception.
     *
     * @param description the event description
     * @param exception   the exception associated with the event
     */
    public Event(String description, Exception exception) {
        this(description);
        this.exception = exception;
    }
}
