package org.optaplanner.experiment.stream.impl.bavet.bi;

import java.util.Map;
import java.util.function.BiFunction;

import org.optaplanner.experiment.stream.impl.bavet.BavetConstraintStreamFactory;
import org.optaplanner.experiment.stream.impl.bavet.BavetConstraintStreamingSession;
import org.optaplanner.experiment.stream.impl.bavet.uni.BavetConstraintStream;
import org.optaplanner.experiment.stream.impl.bavet.uni.BavetConstraintStreaming;
import org.optaplanner.experiment.stream.impl.bavet.uni.BavetScoringConstraintStreaming;

public class BavetScoringBiConstraintStream<A, B> extends BavetBiConstraintStream<A, B> {

    private final String constraintName;
    private final BiFunction<A, B, Long> matchWeighter;

    public BavetScoringBiConstraintStream(BavetConstraintStreamFactory factory,
            String constraintName, BiFunction<A, B, Long> matchWeighter) {
        super(factory);
        this.constraintName = constraintName;
        this.matchWeighter = matchWeighter;
    }

    @Override
    public BavetScoringBiConstraintStreaming<A, B> buildStreamingToNext(
            BavetConstraintStreamingSession session, Map<Object, Object> mergeLinkMap,
            BavetBiConstraintStreaming<A, B> nextStreaming) {
        if (nextStreaming != null) {
            throw new IllegalStateException("Impossible state: the stream (" + this + ") has one ore more nextStreams ("
                    + nextStreamList + ") but it's an endpoint.");
        }
        return new BavetScoringBiConstraintStreaming<A, B>(constraintName, matchWeighter, session);
    }

}
