package org.optaplanner.experiment.bavet;

public class BavetSelectStream<A> implements BavetStream<A>{

    private final BavetFactory factory;
    private final Class<A> selectClass;

    private BavetStream<A> nextStream;

    public BavetSelectStream(BavetFactory factory, Class<A> selectClass) {
        this.factory = factory;
        this.selectClass = selectClass;
    }

    @Override
    public void insert(Object fact) { // TODO HACK: Objects that are not A instances pass through here
        if (selectClass.isInstance(fact)) {
            nextStream.insert((A) fact);
        }
    }

    @Override
    public void retract(A fact) { // TODO HACK: Objects that are not A instances pass through here
        if (selectClass.isInstance(fact)) {
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
