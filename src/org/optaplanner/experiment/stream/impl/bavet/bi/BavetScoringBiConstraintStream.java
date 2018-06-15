package org.optaplanner.experiment.stream.impl.bavet.bi;

import org.optaplanner.experiment.stream.impl.bavet.BavetConstraintStreamFactory;
import org.optaplanner.experiment.stream.impl.bavet.BavetConstraintStreamingSession;
import org.optaplanner.experiment.stream.impl.bavet.uni.BavetConstraintStream;
import org.optaplanner.experiment.stream.impl.bavet.uni.BavetConstraintStreaming;
import org.optaplanner.experiment.stream.impl.bavet.uni.BavetScoringConstraintStreaming;

public class BavetScoringBiConstraintStream<A, B> extends BavetBiConstraintStream<A, B> {

    private final String constraintName;

    public BavetScoringBiConstraintStream(BavetConstraintStreamFactory factory, String constraintName) {
        super(factory);
        this.constraintName = constraintName;
    }

    @Override
    public BavetScoringBiConstraintStreaming<A, B> buildStreamingToNext(
            BavetConstraintStreamingSession session, BavetBiConstraintStreaming<A, B> nextStreaming) {
        if (nextStreaming != null) {
            throw new IllegalStateException("The stream (" + this + ") has one ore more nextStreams ("
                    + nextStreamList + ") but it's an endpoint.");
        }
        return new BavetScoringBiConstraintStreaming<A, B>(constraintName, session);
    }

}
