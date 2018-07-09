package org.optaplanner.experiment.stream.impl.bavet.bi;

import java.util.Map;

import org.optaplanner.experiment.stream.impl.bavet.BavetConstraintStreamFactory;
import org.optaplanner.experiment.stream.impl.bavet.BavetConstraintStreamingSession;

public class BavetJoinBiConstraintStream<A, B, R> extends BavetBiConstraintStream<A, B> {

    public BavetJoinBiConstraintStream(BavetConstraintStreamFactory factory) {
        super(factory);
    }

    @Override
    protected BavetBiConstraintStreaming<A, B> buildStreamingToNext(
            BavetConstraintStreamingSession session, Map<Object, Object> mergeLinkMap,
            BavetBiConstraintStreaming<A, B> nextStreaming) {
        if (nextStreaming == null) {
            throw new IllegalStateException("The stream (" + this + ") leads to nowhere.\n"
                    + "Maybe don't create it.");
        }
        return new BavetJoinBiConstraintStreaming<>(nextStreaming);
    }

}
