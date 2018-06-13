package org.optaplanner.experiment.example;

public class Unavailability {

    private String name;
    private int timeStart;
    private int timeEnd;

    public Unavailability(String name, int timeStart, int timeEnd) {
        this.name = name;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(int timeStart) {
        this.timeStart = timeStart;
    }

    public int getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(int timeEnd) {
        this.timeEnd = timeEnd;
    }

}
