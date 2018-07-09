package org.optaplanner.experiment.stream.impl;

public interface ConstraintStreamingSession {

    void insert(Object fact);

    void retract(Object fact);

    long calculateScore();

}
