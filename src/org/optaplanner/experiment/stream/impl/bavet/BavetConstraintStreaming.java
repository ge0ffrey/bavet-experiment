package org.optaplanner.experiment.stream.impl.bavet;

public abstract class BavetConstraintStreaming<A> {

    public abstract void insert(A fact);
    public abstract void retract(A fact);

}