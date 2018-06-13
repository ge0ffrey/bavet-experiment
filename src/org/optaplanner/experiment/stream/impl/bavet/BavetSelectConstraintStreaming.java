package org.optaplanner.experiment.stream.impl.bavet;

public class BavetSelectConstraintStreaming<A> extends BavetConstraintStreaming<A> {

    private final Class<A> selectClass;
    private final BavetConstraintStreaming<A> nextStreaming;

    public BavetSelectConstraintStreaming(Class<A> selectClass, BavetConstraintStreaming<A> nextStreaming) {
        this.selectClass = selectClass;
        this.nextStreaming = nextStreaming;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void insert(Object fact) {
        if (selectClass.isInstance(fact)) {
            nextStreaming.insert((A) fact);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void retract(Object fact) {
        if (selectClass.isInstance(fact)) {
            nextStreaming.retract((A) fact);
        }
    }

}
