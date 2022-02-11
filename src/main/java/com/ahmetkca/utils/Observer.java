package com.ahmetkca.utils;

public abstract class Observer {
    protected Subject subject;
    public abstract void updateObs();
    protected void setSubject(Subject subject) {
        if (subject == null) {
            return;
        }
        this.subject = subject;
        this.subject.add(this);
    }
}
