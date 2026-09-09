package com.eventmgmt.patterns.behavioral.strategy;

public class ClubMemberPricingStrategy implements PricingStrategy {
    @Override
    public double calculateFee(double basePrice) {
        return basePrice * 0.80;
    }
}