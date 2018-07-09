package org.optaplanner.experiment.stream.impl.bavet.uni;

import java.util.Map;
import java.util.function.Function;

import org.optaplanner.experiment.stream.impl.bavet.BavetConstraintStreamFactory;
import org.optaplanner.experiment.stream.impl.bavet.BavetConstraintStreamingSession;

public class BavetScoringConstraintStream<A> extends BavetConstraintStream<A> {

    private final String constraintName;
    private final Function<A, Long> matchWeighter;

    public BavetScoringConstraintStream(BavetConstraintStreamFactory factory,
            String constraintName, Function<A, Long> matchWeighter) {
        super(factory);
        this.constraintName = constraintName;
        this.matchWeighter = matchWeighter;
    }

    @Override
    public BavetScoringConstraintStreaming<A> buildStreamingToNext(
            BavetConstraintStreamingSession session, Map<Object, Object> mergeLinkMap,
            BavetConstraintStreaming<A> nextStreaming) {
        if (nextStreaming != null) {
            throw new IllegalStateException("Impossible state: the stream (" + this + ") has one ore more nextStreams ("
                    + nextStreamList + ") but it's an endpoint.");
        }
        return new BavetScoringConstraintStreaming<A>(constraintName, matchWeighter, session);
    }

}
