package org.optaplanner.experiment.bavet;

import java.util.function.Predicate;

public class BavetFilterStream<A> implements BavetStream<A>{

    private final BavetFactory factory;
    private final Predicate<A> predicate;

    private BavetStream<A> nextStream;

    public BavetFilterStream(BavetFactory factory, Predicate<A> predicate) {
        this.factory = factory;
        this.predicate = predicate;
    }

    @Override
    public void insert(A fact) {
        if (predicate.test(fact)) {
            nextStream.insert(fact);
        }
    }

    @Override
    public void retract(A fact) {
        if (predicate.test(fact)) {
            nextStream.retract(fact);
        }
    }

    @Override
    public BavetFactory getFactory() {
        return factory;
    }

    @Override
    public void setNextStream(BavetStream<A> nextStream) {
        this.nextStream = nextStream;
    }

}
