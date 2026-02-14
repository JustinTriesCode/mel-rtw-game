package model;

import java.util.Random;

public class Equation {
    private int value1;
    private int value2;
    private char operator; // +, -, *, /

    public void generateEquation() {
        value1 = new Random().nextInt(10) + 1;
        value2 = new Random().nextInt(10) + 1;
        operator = "+-*/".charAt(new Random().nextInt(4));

    }
    // TODO: Implement logic to generate random equations and evaluate them
}
