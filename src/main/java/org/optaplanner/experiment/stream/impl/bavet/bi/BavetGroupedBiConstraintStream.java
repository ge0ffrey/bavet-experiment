package org.optaplanner.experiment.stream.impl.bavet.bi;

import java.util.Map;

import org.optaplanner.experiment.stream.impl.bavet.BavetConstraintStreamFactory;
import org.optaplanner.experiment.stream.impl.bavet.BavetConstraintStreamingSession;

public class BavetGroupedBiConstraintStream<GroupKey_, Result_> extends BavetBiConstraintStream<GroupKey_, Result_> {

    public BavetGroupedBiConstraintStream(BavetConstraintStreamFactory factory) {
        super(factory);
    }

    @Override
    protected BavetBiConstraintStreaming<GroupKey_, Result_> buildStreamingToNext(
            BavetConstraintStreamingSession session, Map<Object, Object> mergeLinkMap,
            BavetBiConstraintStreaming<GroupKey_, Result_> nextStreaming) {
        if (nextStreaming == null) {
            throw new IllegalStateException("The stream (" + this + ") leads to nowhere.\n"
                    + "Maybe don't create it.");
        }
        return new BavetGroupedBiConstraintStreaming<>(nextStreaming);
    }

}
