package com.eventmgmt.patterns.behavioral.strategy;

public interface PricingStrategy {
    double calculateFee(double basePrice);
}