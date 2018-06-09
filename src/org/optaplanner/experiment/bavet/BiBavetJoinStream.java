package org.optaplanner.experiment.bavet;

import java.util.function.BiPredicate;

public class BiBavetJoinStream<A, B> implements BiBavetStream<A, B> {


    private final BavetFactory factory;
    private final BiPredicate<A, B> predicate;

    public BiBavetJoinStream(BavetFactory factory, BiPredicate<A, B> predicate) {
        this.factory = factory;
        this.predicate = predicate;
    }

    @Override
    public void insert(A a, B b) {
        ggg;
    }

    @Override
    public void retract(A a, B b) {
        ggg;
    }

    private BiBavetStream<A, B> nextStream;


    @Override
    public BavetFactory getFactory() {
        return factory;
    }

    @Override
    public void setNextStream(BiBavetStream<A, B> nextStream) {
        this.nextStream = nextStream;
    }

}
