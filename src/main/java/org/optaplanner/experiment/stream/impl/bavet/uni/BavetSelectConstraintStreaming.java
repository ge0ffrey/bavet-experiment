package org.optaplanner.experiment.stream.impl.bavet.uni;

public class BavetSelectConstraintStreaming<A> extends BavetConstraintStreaming<A> {

    private final Class<A> selectClass;
    private final BavetConstraintStreaming<A> nextStreaming;

    public BavetSelectConstraintStreaming(Class<A> selectClass, BavetConstraintStreaming<A> nextStreaming) {
        this.selectClass = selectClass;
        this.nextStreaming = nextStreaming;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void insert(Object a) {
        if (selectClass.isInstance(a)) {
            nextStreaming.insert((A) a);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void retract(Object a) {
        if (selectClass.isInstance(a)) {
            nextStreaming.retract((A) a);
        }
    }

}
