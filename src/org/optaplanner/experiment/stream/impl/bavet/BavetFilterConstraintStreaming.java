package org.optaplanner.experiment.stream.impl.bavet;

import java.util.function.Predicate;

public class BavetFilterConstraintStreaming<A> extends BavetConstraintStreaming<A> {

    private final Predicate<A> predicate;
    private final BavetConstraintStreaming<A> nextStreaming;

    public BavetFilterConstraintStreaming(Predicate<A> predicate, BavetConstraintStreaming<A> nextStreaming) {
        this.predicate = predicate;
        this.nextStreaming = nextStreaming;
    }

    @Override
    public void insert(A fact) {
        if (predicate.test(fact)) {
            nextStreaming.insert(fact);
        }
    }

    public void retract(A fact) {
        if (predicate.test(fact)) {
            nextStreaming.retract(fact);
        }
    }

}
