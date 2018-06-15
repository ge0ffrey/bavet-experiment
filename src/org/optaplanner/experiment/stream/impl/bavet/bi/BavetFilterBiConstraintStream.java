package org.optaplanner.experiment.stream.impl.bavet.bi;

import java.util.function.BiPredicate;
import java.util.function.Predicate;

import org.optaplanner.experiment.stream.impl.bavet.BavetConstraintStreamFactory;
import org.optaplanner.experiment.stream.impl.bavet.BavetConstraintStreamingSession;
import org.optaplanner.experiment.stream.impl.bavet.uni.BavetConstraintStream;
import org.optaplanner.experiment.stream.impl.bavet.uni.BavetConstraintStreaming;
import org.optaplanner.experiment.stream.impl.bavet.uni.BavetFilterConstraintStreaming;

public class BavetFilterBiConstraintStream<A, B> extends BavetBiConstraintStream<A, B> {

    private final BiPredicate<A, B> predicate;

    public BavetFilterBiConstraintStream(BavetConstraintStreamFactory factory, BiPredicate<A, B> predicate) {
        super(factory);
        this.predicate = predicate;
    }

    @Override
    public BavetFilterBiConstraintStreaming<A, B> buildStreamingToNext(
            BavetConstraintStreamingSession session, BavetBiConstraintStreaming<A, B> nextStreaming) {
        if (nextStreaming == null) {
            throw new IllegalStateException("The stream (" + this + ") leads to nowhere.\n"
                    + "Maybe don't create it.");
        }
        return new BavetFilterBiConstraintStreaming<>(predicate, nextStreaming);
    }

}
