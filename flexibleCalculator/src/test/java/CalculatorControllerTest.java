import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.example.flexiblecalculator.controller.CalculatorController;
import com.example.flexiblecalculator.enums.Operation;
import com.example.flexiblecalculator.services.Calculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class CalculatorControllerTest {

    @Mock
    private Calculator calculator;

    @InjectMocks
    private CalculatorController calculatorController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCalculateCore() {
        Operation operation = Operation.ADD;
        double num1 = 10;
        double num2 = 20;

        when(calculator.calculate(operation, num1, num2)).thenReturn(30.0);

        double result = calculatorController.calculate(operation, num1, num2);

        assertEquals(30.0, result);
        verify(calculator, times(1)).calculate(operation, num1, num2);
    }

    @Test
    void testCalculateDynamic() {
        String operation = "ADD";
        Double[] args = {10.0, 20.0};

        when(calculator.dynamicCalculate(operation, args)).thenReturn(30.0);

        double result = calculatorController.calculate(operation, args);

        assertEquals(30.0, result);
        verify(calculator, times(1)).dynamicCalculate(operation, args);
    }

    @Test
    void testChainCalculate() {
        double initial = 10.0;
        Object[] opsAndValues = {"ADD", 20.0, "MULTIPLY", 2.0};

        when(calculator.chainCalculate(initial, opsAndValues)).thenReturn(60.0);

        double result = calculatorController.chainCalculate(initial, opsAndValues);

        assertEquals(60.0, result);
        verify(calculator, times(1)).chainCalculate(initial, opsAndValues);
    }

    @Test
    void testAddOperation() {
        String operation = "CUSTOM_OP";
        String func = "a + b";

        doNothing().when(calculator).addOperation(operation, func);

        String result = calculatorController.addOperation(operation, func);

        assertEquals("Operation added successfully", result);
        verify(calculator, times(1)).addOperation(operation, func);
    }

    @Test
    void testCalculateCoreInvalidOperation() {
        when(calculator.calculate(any(), anyDouble(), anyDouble()))
                .thenThrow(new UnsupportedOperationException("Invalid operation"));

        UnsupportedOperationException exception = assertThrows(UnsupportedOperationException.class, () -> {
            calculatorController.calculate(Operation.ADD, 10, 20);
        });

        assertEquals("Invalid operation", exception.getMessage());
    }

    @Test
    void testCalculateDynamicInvalidOperation() {
        String operation = "INVALID_OP";
        Double[] args = {10.0, 20.0};

        when(calculator.dynamicCalculate(operation, args))
                .thenThrow(new UnsupportedOperationException("Operation not supported: " + operation));

        UnsupportedOperationException exception = assertThrows(UnsupportedOperationException.class, () -> {
            calculatorController.calculate(operation, args);
        });

        assertEquals("Operation not supported: " + operation, exception.getMessage());
    }

    @Test
    void testChainCalculateInvalidOperation() {
        double initial = 10.0;
        Object[] opsAndValues = {"INVALID_OP", 20.0, "MULTIPLY", 2.0};

        when(calculator.chainCalculate(initial, opsAndValues))
                .thenThrow(new IllegalArgumentException("Invalid Operation value at index 0"));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            calculatorController.chainCalculate(initial, opsAndValues);
        });

        assertEquals("Invalid Operation value at index 0", exception.getMessage());
    }
}

