package org.optaplanner.experiment.bavet;

public class BavetEndpoint<A> implements BavetStream<A> { // TODO should not implement BavetStream (problem with setNextStream hack)

    private final BavetFactory factory;
    private final String constraintName;

    private long score = 0L;

    public BavetEndpoint(BavetFactory factory, String constraintName) {
        this.factory = factory;
        this.constraintName = constraintName;
    }

    @Override
    public void insert(A fact) {
        score--;
        factory.adjustScore(-1L);
    }

    @Override
    public void retract(A fact) {
        score++;
        factory.adjustScore(1L);
    }

    @Override
    public BavetFactory getFactory() {
        return factory;
    }

    @Override
    public void setNextStream(BavetStream<A> nextStream) {
        throw new UnsupportedOperationException();
    }

}
