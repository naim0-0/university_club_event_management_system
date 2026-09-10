package com.eventmgmt.patterns.behavioral.strategy;

public class EarlyBirdPricingStrategy implements PricingStrategy {
    @Override
    public double calculateFee(double basePrice) {
        return basePrice * 0.90;
    }
}