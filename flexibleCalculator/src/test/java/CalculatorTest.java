import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.flexiblecalculator.enums.Operation;
import com.example.flexiblecalculator.services.Calculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CalculatorTest {

    private Calculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new Calculator();
    }

    @Test
    void testCalculateAdd() {
        double result = calculator.calculate(Operation.ADD, 10, 20);
        assertEquals(30, result);
    }

    @Test
    void testCalculateSubtract() {
        double result = calculator.calculate(Operation.SUBTRACT, 20, 10);
        assertEquals(10, result);
    }

    @Test
    void testCalculateMultiply() {
        double result = calculator.calculate(Operation.MULTIPLY, 10, 20);
        assertEquals(200, result);
    }

    @Test
    void testCalculateDivide() {
        double result = calculator.calculate(Operation.DIVIDE, 20, 10);
        assertEquals(2, result);
    }

    @Test
    void testCalculateDivideByZero() {
        ArithmeticException exception = assertThrows(ArithmeticException.class, () -> {
            calculator.calculate(Operation.DIVIDE, 20, 0);
        });

        assertEquals("Cannot divide by zero", exception.getMessage());
    }

    @Test
    void testCalculateInvalidOperation() {
        UnsupportedOperationException exception = assertThrows(UnsupportedOperationException.class, () -> {
            calculator.calculate(null, 10, 20);
        });

        assertEquals("Operation not supported: null", exception.getMessage());
    }

    @Test
    void testChainCalculate() {
        Object[] opsAndValues = {"ADD", 20.0, "MULTIPLY", 2.0};
        double result = calculator.chainCalculate(10.0, opsAndValues);
        assertEquals(60.0, result);
    }

    @Test
    void testChainCalculateInvalidOperation() {
        Object[] opsAndValues = {"INVALID_OP", 20.0, "MULTIPLY", 2.0};

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            calculator.chainCalculate(10.0, opsAndValues);
        });

        assertEquals("Invalid Operation value at index 0", exception.getMessage());
    }

    @Test
    void testChainCalculateInvalidInputs() {
        Object[] opsAndValues = {"ADD", 20.0, "MULTIPLY"};

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            calculator.chainCalculate(10.0, opsAndValues);
        });

        assertEquals("Chaining inputs must alternate between Operation and Number", exception.getMessage());
    }

    @Test
    void testAddOperationAndDynamicCalculate() {
        calculator.addOperation("MODULUS", "a % b");
        Double[] args = {10.0, 3.0};
        double result = calculator.dynamicCalculate("MODULUS", args);
        assertEquals(1.0, result);
    }

    @Test
    void testAddOperationInvalidFunction() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            calculator.addOperation("CUSTOM_OP", "a +");
        });

        assertEquals("The provided function is invalid or incomplete.", exception.getMessage());
    }


    @Test
    void testDynamicCalculateInvalidOperation() {
        UnsupportedOperationException exception = assertThrows(UnsupportedOperationException.class, () -> {
            calculator.dynamicCalculate("INVALID_OP", 10.0, 20.0);
        });

        assertEquals("Operation not supported: INVALID_OP", exception.getMessage());
    }

    @Test
    void testDynamicCalculateInvalidInputs() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            calculator.dynamicCalculate("ADD", (Double[]) null);
        });

        assertEquals("Inputs cannot be null or empty", exception.getMessage());
    }

    @Test
    void testDynamicCalculateNullArgument() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            calculator.dynamicCalculate("ADD", 10.0, null);
        });

        assertEquals("Inputs cannot be null", exception.getMessage());
    }
}
