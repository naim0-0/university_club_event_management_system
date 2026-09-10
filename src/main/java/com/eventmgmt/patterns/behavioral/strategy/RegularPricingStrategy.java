package com.eventmgmt.patterns.behavioral.strategy;

public class RegularPricingStrategy implements PricingStrategy {
    @Override
    public double calculateFee(double basePrice) {
        return basePrice;
    }
}