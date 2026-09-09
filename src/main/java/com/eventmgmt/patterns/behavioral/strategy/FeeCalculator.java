package com.eventmgmt.patterns.behavioral.strategy;

public class FeeCalculator {
    private PricingStrategy strategy;

    public FeeCalculator(PricingStrategy strategy) {
        this.strategy = strategy;
    }

    public void setStrategy(PricingStrategy strategy) {
        this.strategy = strategy;
    }

    public double calculate(double basePrice) {
        return strategy.calculateFee(basePrice);
    }
}