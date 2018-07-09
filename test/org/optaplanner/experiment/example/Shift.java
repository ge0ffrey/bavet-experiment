package org.optaplanner.experiment.example;

public class Shift {

    private int time;
    private long difficulty;

    private String personName;

    public Shift(int time, String personName) {
        this(time, 0L, personName);
    }

    public Shift(int time, long difficulty, String personName) {
        this.time = time;
        this.difficulty = difficulty;
        this.personName = personName;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public long getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(long difficulty) {
        this.difficulty = difficulty;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }
}
