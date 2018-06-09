package org.optaplanner.experiment.bavet;

public class BiBavetEndpoint<A, B> implements BiBavetStream<A, B> { // TODO should not implement BavetStream (problem with setNextStream hack)

    private final BavetFactory factory;
    private final String constraintName;

    private long score = 0L;

    public BiBavetEndpoint(BavetFactory factory, String constraintName) {
        this.factory = factory;
        this.constraintName = constraintName;
    }

    @Override
    public void insert(A a, B b) {
        score--;
        factory.adjustScore(-1L);
    }

    @Override
    public void retract(A a, B b) {
        score++;
        factory.adjustScore(1L);
    }

    @Override
    public BavetFactory getFactory() {
        return factory;
    }

    @Override
    public void setNextStream(BiBavetStream<A, B> nextStream) {
        throw new UnsupportedOperationException();
    }

}
