package main.java.com.justintriescode.mellysgame.model;

import java.util.Random;

public class Equation {
    public enum EquationType {
        SIMPLE, COMPLEX
    };

    private int value1;
    private int value2;
    private char operator;
    private Random rand = new Random();
    private EquationType type;

    // EFFECTS: constructs an equation of the given type with random values up to max
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

    // EFFECTS: returns a string representation of the equation
    public String toString() {
        if (type == EquationType.SIMPLE) {
            return Integer.toString(value1);
        } else {
            return value1 + " " + operator + " " + value2;
        }
    }

    // EFFECTS: evaluates the equation and returns the result
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

    // EFFECTS: compares this equation's value to another's, returning true if this
    // is greater
    public boolean compareEquationsGreater(Equation compareWith) {
        return this.evaluate() > compareWith.evaluate();
    }

    // EFFECTS: compares this equation's value to another's, returning true if this
    // is less
    public boolean compareEquationsLess(Equation compareWith) {
        return this.evaluate() < compareWith.evaluate();
    }
}
