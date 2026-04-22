package com.justintriescode.mellysgame.model;

import java.util.Random;

/**
 * Represents a mathematical equation used in the game.
 * Equations can be simple (a single value) or complex (two values with an
 * operator).
 */
public class Equation {
    public enum EquationType {
        SIMPLE, COMPLEX
    };

    private int value1;
    private int value2;
    private char operator;
    private Random rand = new Random();
    private EquationType type;

    /**
     * Constructs an equation of the given type with random values up to the
     * specified maximum.
     *
     * @param type The type of equation to generate (SIMPLE or COMPLEX).
     * @param max  The maximum boundary for the randomly generated values.
     */
    public Equation(EquationType type, int max) {
        this.type = type;
        if (type == EquationType.SIMPLE) {
            value1 = rand.nextInt(max) + 1;
            value2 = 0;
            operator = '+';
        } else {
            value1 = rand.nextInt(max) + 1;
            value2 = rand.nextInt(max) + 1;
            operator = "+-*/".charAt(rand.nextInt(4));
        }
    }

    /**
     * Returns a string representation of the equation.
     * For simple equations, it returns just the numeric value.
     * For complex equations, it returns the full equation formula (e.g., "5 + 3").
     *
     * @return The formatted string representation of the equation.
     */
    @Override
    public String toString() {
        if (type == EquationType.SIMPLE) {
            // Pad simple numbers to at least 3 digits to prevent visual length exploits
            return String.format("%03d", value1);
        } else {
            return value1 + " " + operator + " " + value2;
        }
    }

    /**
     * Evaluates the mathematical equation and returns its integer result.
     *
     * @return The calculated result of the equation.
     */
    public int evaluate() {
        switch (operator) {
            case '+':
                return value1 + value2;
            case '-':
                return value1 - value2;
            case '*':
                return value1 * value2;
            case '/':
                return value2 != 0 ? value1 / value2 : 0;
        }
        return value1;
    }

    /**
     * Compares this equation's evaluated result to another equation's result.
     *
     * @param compareWith The equation to compare against.
     * @return true if this equation's value is strictly greater than the compared
     *         equation's value.
     */
    public boolean compareEquationsGreater(Equation compareWith) {
        return this.evaluate() > compareWith.evaluate();
    }

    /**
     * Compares this equation's evaluated result to another equation's result.
     *
     * @param compareWith The equation to compare against.
     * @return true if this equation's value is strictly less than the compared
     *         equation's value.
     */
    public boolean compareEquationsLess(Equation compareWith) {
        return this.evaluate() < compareWith.evaluate();
    }
}
