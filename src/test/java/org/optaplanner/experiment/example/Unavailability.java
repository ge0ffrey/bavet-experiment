package org.optaplanner.experiment.example;

public class Unavailability {

    private String personName;
    private int timeStart;
    private int timeEnd;

    public Unavailability(String personName, int timeStart, int timeEnd) {
        this.personName = personName;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
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
