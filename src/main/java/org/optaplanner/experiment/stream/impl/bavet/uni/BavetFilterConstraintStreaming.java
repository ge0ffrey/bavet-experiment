package org.optaplanner.experiment.stream.impl.bavet.uni;

import java.util.function.Predicate;

public class BavetFilterConstraintStreaming<A> extends BavetConstraintStreaming<A> {

    private final Predicate<A> predicate;
    private final BavetConstraintStreaming<A> nextStreaming;

    public BavetFilterConstraintStreaming(Predicate<A> predicate, BavetConstraintStreaming<A> nextStreaming) {
        this.predicate = predicate;
        this.nextStreaming = nextStreaming;
    }

    @Override
    public void insert(A a) {
        if (predicate.test(a)) {
            nextStreaming.insert(a);
        }
    }

    public void retract(A a) {
        if (predicate.test(a)) {
            nextStreaming.retract(a);
        }
    }

}
