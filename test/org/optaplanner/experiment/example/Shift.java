package org.optaplanner.experiment.example;

public class Shift {

    private int time;

    private String personName;

    public Shift(int time, String personName) {
        this.time = time;
        this.personName = personName;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }
}
