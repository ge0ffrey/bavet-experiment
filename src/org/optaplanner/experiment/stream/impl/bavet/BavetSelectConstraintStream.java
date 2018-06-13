package org.optaplanner.experiment.stream.impl.bavet;

public class BavetSelectConstraintStream<A> extends BavetConstraintStream<A> {

    private final Class<A> selectClass;

    public BavetSelectConstraintStream(BavetConstraintStreamFactory factory, Class<A> selectClass) {
        super(factory);
        this.selectClass = selectClass;
    }

    @Override
    public BavetSelectConstraintStreaming<A> buildStreamingToNext(
            BavetConstraintStreamingSession session, BavetConstraintStreaming<A> nextStreaming) {
        if (nextStreaming == null) {
            throw new IllegalStateException("The stream (" + this + ") leads to nowhere.\n"
                    + "Maybe don't create it.");
        }
        return new BavetSelectConstraintStreaming<A>(selectClass, nextStreaming);
    }

}
