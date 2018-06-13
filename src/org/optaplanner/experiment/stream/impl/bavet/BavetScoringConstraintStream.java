package org.optaplanner.experiment.stream.impl.bavet;

public class BavetScoringConstraintStream<A> extends BavetConstraintStream<A> {

    private final String constraintName;

    public BavetScoringConstraintStream(BavetConstraintStreamFactory factory, String constraintName) {
        super(factory);
        this.constraintName = constraintName;
    }

    @Override
    public BavetScoringConstraintStreaming<A> buildStreamingToNext(
            BavetConstraintStreamingSession session, BavetConstraintStreaming<A> nextStreaming) {
        if (nextStreaming != null) {
            throw new IllegalStateException("The stream (" + this + ") has one ore more nextStreams ("
                    + nextStreamList + ") but it's an endpoint.");
        }
        return new BavetScoringConstraintStreaming<A>(constraintName, session);
    }

}
