package com.eventmgmt.patterns.behavioral.observer;

public class StudentObserver implements EventObserver {
    private final String studentName;
    private final String email;

    public StudentObserver(String studentName, String email) {
        this.studentName = studentName;
        this.email = email;
    }

    @Override
    public void update(String message) {
        System.out.println("[Notification Sent] To: " + studentName + " (" + email + ") -> " + message);
    }
}