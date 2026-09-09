package com.eventmgmt.patterns.behavioral.observer;

import java.util.ArrayList;
import java.util.List;

public class EventNotifier {
    private final List<EventObserver> observers = new ArrayList<>();

    public void subscribe(EventObserver observer) {
        observers.add(observer);
    }

    public void unsubscribe(EventObserver observer) {
        observers.remove(observer);
    }

    public void notifyAllObservers(String message) {
        for (EventObserver observer : observers) {
            observer.update(message);
        }
    }
}