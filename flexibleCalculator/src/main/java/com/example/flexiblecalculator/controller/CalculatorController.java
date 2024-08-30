package com.example.flexiblecalculator.controller;

import com.example.flexiblecalculator.enums.Operation;
import com.example.flexiblecalculator.services.Calculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/calculator")
public class CalculatorController {
    private final Calculator calculator;

    @Autowired
    public CalculatorController(Calculator calculator) {
        this.calculator = calculator;
    }

    @GetMapping("/calculate/core")
    public double calculate(
            @RequestParam Operation operation,
            @RequestParam double num1,
            @RequestParam double num2) {
        return calculator.calculate(operation, num1, num2);
    }

    @GetMapping("/calculate/dynamic")
    public double calculate(
            @RequestParam String operation,
            @RequestParam Double... args) {
        return calculator.dynamicCalculate(operation, args);
    }

    @PostMapping("/chainCalculate")
    public double chainCalculate(@RequestParam double initial, @RequestBody Object[] opsAndValues) {
        return calculator.chainCalculate(initial, opsAndValues);
    }

    @PostMapping("/addOperation")
    public String addOperation(@RequestParam String operation, @RequestBody String func) {
        calculator.addOperation(operation, func);
        return "Operation added successfully";
    }
}
