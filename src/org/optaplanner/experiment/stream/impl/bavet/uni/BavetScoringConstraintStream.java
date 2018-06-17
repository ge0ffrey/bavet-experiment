package org.optaplanner.experiment.stream.impl.bavet.uni;

import java.util.Map;

import org.optaplanner.experiment.stream.impl.bavet.BavetConstraintStreamFactory;
import org.optaplanner.experiment.stream.impl.bavet.BavetConstraintStreamingSession;

public class BavetScoringConstraintStream<A> extends BavetConstraintStream<A> {

    private final String constraintName;

    public BavetScoringConstraintStream(BavetConstraintStreamFactory factory, String constraintName) {
        super(factory);
        this.constraintName = constraintName;
    }

    @Override
    public BavetScoringConstraintStreaming<A> buildStreamingToNext(
            BavetConstraintStreamingSession session, Map<Object, Object> mergeLinkMap,
            BavetConstraintStreaming<A> nextStreaming) {
        if (nextStreaming != null) {
            throw new IllegalStateException("Impossible state: the stream (" + this + ") has one ore more nextStreams ("
                    + nextStreamList + ") but it's an endpoint.");
        }
        return new BavetScoringConstraintStreaming<A>(constraintName, session);
    }

}
