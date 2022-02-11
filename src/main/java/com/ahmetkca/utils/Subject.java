package com.ahmetkca.utils;

import java.util.ArrayList;
import java.util.List;

public class Subject {
    public List<Observer> getObserverList() {
        return observerList;
    }

    private List<Observer> observerList = new ArrayList<>();
    private int state;

    public void add(Observer observer) {
        observerList.add(observer);
    }
    public int getState() {
        return state;
    }
    public void setState(int state) {
        this.state = state;
        execute();
    }

    private void execute() {
        for (Observer observer: observerList) {
            observer.updateObs();
        }
    }
}
