package test;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;

import static org.junit.jupiter.api.Assertions.*;
import model.Equation;
import model.Equation.EquationType;

public class equationTest {

    @RepeatedTest(20)
    public void testSimple() {
        Equation eq = new Equation(EquationType.SIMPLE,10);
        int result = eq.evaluate();
        assertTrue(result > 0 && result <= 10);
    }

    @RepeatedTest(50)
    public void testComplex() {
        Equation eq = new Equation(EquationType.COMPLEX,5);
        int result = eq.evaluate();
        assertTrue(result >= -4 && result <= 25);
    }
}
