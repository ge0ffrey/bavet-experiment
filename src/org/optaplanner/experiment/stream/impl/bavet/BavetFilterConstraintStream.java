package org.optaplanner.experiment.stream.impl.bavet;

import java.util.function.Predicate;

public class BavetFilterConstraintStream<A> extends BavetConstraintStream<A> {

    private final Predicate<A> predicate;

    public BavetFilterConstraintStream(BavetConstraintStreamFactory factory, Predicate<A> predicate) {
        super(factory);
        this.predicate = predicate;
    }

    @Override
    public BavetFilterConstraintStreaming<A> buildStreamingToNext(
            BavetConstraintStreamingSession session, BavetConstraintStreaming<A> nextStreaming) {
        if (nextStreaming == null) {
            throw new IllegalStateException("The stream (" + this + ") leads to nowhere.\n"
                    + "Maybe don't create it.");
        }
        return new BavetFilterConstraintStreaming<A>(predicate, nextStreaming);
    }

}
