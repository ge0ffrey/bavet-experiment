package org.optaplanner.experiment.stream.api.collector;

import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.stream.Collector;

/**
 * Similar to {@link java.util.stream.Collector}, but with disperser (anti-accumulater) functionality,
 * without combiner functionality.
 */
public interface ConstraintStreamCollector<T, ResultContainer_, Result_> extends Collector<T, ResultContainer_, Result_> {

    BiConsumer<ResultContainer_, T> disperser();

    @Override
    default BinaryOperator<ResultContainer_> combiner() {
        throw new UnsupportedOperationException();
    }

    @Override
    default Set<Characteristics> characteristics() {
        throw new UnsupportedOperationException();
    }

}
