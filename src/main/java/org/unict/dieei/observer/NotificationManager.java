package org.unict.dieei.observer;

import java.util.ArrayList;
import java.util.List;

public class NotificationManager {
    private static List<Observer> observers = new ArrayList<>();

    public static void addObserver(Observer observer) {
        observers.add(observer);
    }

    public static void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    public static void notifyObservers(String message) {
        for (Observer observer : observers) {
            observer.update(message);
        }
    }

}
