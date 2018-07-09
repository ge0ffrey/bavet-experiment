package org.optaplanner.experiment.stream.impl.bavet.uni;

import java.util.Map;
import java.util.function.Predicate;

import org.optaplanner.experiment.stream.impl.bavet.BavetConstraintStreamFactory;
import org.optaplanner.experiment.stream.impl.bavet.BavetConstraintStreamingSession;

public class BavetFilterConstraintStream<A> extends BavetConstraintStream<A> {

    private final Predicate<A> predicate;

    public BavetFilterConstraintStream(BavetConstraintStreamFactory factory, Predicate<A> predicate) {
        super(factory);
        this.predicate = predicate;
    }

    @Override
    public BavetFilterConstraintStreaming<A> buildStreamingToNext(
            BavetConstraintStreamingSession session, Map<Object, Object> mergeLinkMap,
            BavetConstraintStreaming<A> nextStreaming) {
        if (nextStreaming == null) {
            throw new IllegalStateException("The stream (" + this + ") leads to nowhere.\n"
                    + "Maybe don't create it.");
        }
        return new BavetFilterConstraintStreaming<>(predicate, nextStreaming);
    }

}
