package org.optaplanner.experiment.stream.impl.bavet.bi;

public abstract class BavetBiConstraintStreaming<A, B> {

    public abstract void insert(A a, B b);
    public abstract void retract(A a, B b);

}
