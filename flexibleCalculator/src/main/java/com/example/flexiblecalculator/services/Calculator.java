package com.example.flexiblecalculator.services;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import org.springframework.stereotype.Service;
import com.example.flexiblecalculator.enums.Operation;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

@Service
public class Calculator {
    // Store enum-based operations
    private final Map<Operation, BiFunction<Double, Double, Double>> operations;

    // Store dynamically added operations
    private final Map<String, Function<Double[], Double>> dynamicOperations;

    // Constructor
    public Calculator() {
        operations = new HashMap<>();
        dynamicOperations = new HashMap<>();

        // Initialize the basic operations using the enum
        for (Operation op : Operation.values()) {
            operations.put(op, op::apply);
        }
    }

    // Basic Calculation Method using Enum Operations
    public double calculate(Operation op, Number num1, Number num2) {
        validateInput(num1, num2); // Ensure inputs are not null
        BiFunction<Double, Double, Double> operation = operations.get(op);
        if (operation == null) {
            throw new UnsupportedOperationException("Operation not supported: " + op);
        }
        return operation.apply(num1.doubleValue(), num2.doubleValue());
    }

    // Validation method for basic calculations
    private void validateInput(Number num1, Number num2) {
        if (num1 == null || num2 == null) {
            throw new IllegalArgumentException("Inputs cannot be null");
        }
    }

    // Chaining Operations Method
    public double chainCalculate(double initial, Object... opsAndValues) {
        validateChainInputs(opsAndValues); // Ensure correct input format
        double result = initial;
        for (int i = 0; i < opsAndValues.length; i += 2) {
            Operation op = Operation.valueOf((String) opsAndValues[i]); // Convert string to enum
            Number value = (Number) opsAndValues[i + 1];
            result = calculate(op, result, value);
        }
        return result;
    }

    // Validation method for chaining operations
    private void validateChainInputs(Object[] opsAndValues) {
        if (opsAndValues.length % 2 != 0) {
            throw new IllegalArgumentException("Chaining inputs must alternate between Operation and Number");
        }
        for (int i = 0; i < opsAndValues.length; i += 2) {
            if (!(opsAndValues[i] instanceof String)) {
                throw new IllegalArgumentException("Expected an Operation name (String) at index " + i);
            }
            try {
                Operation.valueOf((String) opsAndValues[i]); // Check if the string can be converted to an enum
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid Operation value at index " + i);
            }
            if (!(opsAndValues[i + 1] instanceof Number)) {
                throw new IllegalArgumentException("Expected a Number at index " + (i + 1));
            }
        }
    }

    // New added operation calculation method
    public double dynamicCalculate(String operationName, Double... args) {
        validateInput(args); // Ensure inputs are not null
        Function<Double[], Double> operation = dynamicOperations.get(operationName.toUpperCase());
        if (operation == null) {
            throw new UnsupportedOperationException("Operation not supported: " + operationName);
        }
        return operation.apply(args);
    }

    // Extensibility: Adding new operations without changing existing code
    // Method to add a new operation dynamically
    public void addOperation(String operationName, String func) {
        if (operationName == null || func == null) {
            throw new IllegalArgumentException("Operation name and function cannot be null");
        }

        Function<Double[], Double> function = createFunctionFromLogic(func);
        dynamicOperations.put(operationName.toUpperCase(), function);
    }

    // Helper method to create a BiFunction from a user-provided string
    private Function<Double[], Double> createFunctionFromLogic(String func) {
        // First, validate the function string
        if (!isValidFunction(func)) {
            throw new IllegalArgumentException("The provided function is invalid or incomplete.");
        }
        return args -> {
            try {
                // Build expression and set variables dynamically
                ExpressionBuilder builder = new ExpressionBuilder(func);
                for (int i = 0; i < args.length; i++) {
                    builder.variable(Character.toString((char)('a' + i)));
                }
                Expression expression = builder.build();
                for (int i = 0; i < args.length; i++) {
                    expression.setVariable(Character.toString((char)('a' + i)), args[i]);
                }
                return expression.evaluate();
            } catch (Exception e) {
                throw new IllegalArgumentException("Invalid function expression: " + func, e);
            }
        };
    }

    private boolean isValidFunction(String func) {

        func = func.trim();

        // Basic validation: Reject forbidden characters
        if (func.matches(".*[`~@#$&\\[\\]{};:'\"\\\\].*")) {
            return false;
        }

        // Ensure the function is not too short or too long
        if (func.length() > 100 || func.length() < 1) {
            return false;
        }

        // Check for trailing operators which are likely to indicate an incomplete expression
        if (func.matches(".*[\\+\\-\\*/%]$")) {
            return false;
        }
        return true;
    }

    private void validateInput(Double[] args) {
        if (args == null || args.length == 0) {
            throw new IllegalArgumentException("Inputs cannot be null or empty");
        }
        for (Double arg : args) {
            if (arg == null) {
                throw new IllegalArgumentException("Inputs cannot be null");
            }
        }
    }
}
