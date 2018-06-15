package org.optaplanner.experiment.stream.impl.bavet.bi;

import java.util.function.BiPredicate;
import java.util.function.Predicate;

import org.optaplanner.experiment.stream.impl.bavet.uni.BavetConstraintStreaming;

public class BavetFilterBiConstraintStreaming<A, B> extends BavetBiConstraintStreaming<A, B> {

    private final BiPredicate<A, B> predicate;
    private final BavetBiConstraintStreaming<A, B> nextStreaming;

    public BavetFilterBiConstraintStreaming(BiPredicate<A, B> predicate, BavetBiConstraintStreaming<A, B> nextStreaming) {
        this.predicate = predicate;
        this.nextStreaming = nextStreaming;
    }

    @Override
    public void insert(A a, B b) {
        if (predicate.test(a, b)) {
            nextStreaming.insert(a, b);
        }
    }

    public void retract(A a, B b) {
        if (predicate.test(a, b)) {
            nextStreaming.retract(a, b);
        }
    }

}
