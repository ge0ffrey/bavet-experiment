package org.optaplanner.experiment.stream.api.collector;

import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

/**
 * Similar to {@link java.util.stream.Collector}, but with disperser (anti-accumulater) functionality and
 * without combiner functionality.
 */
public interface ConstraintStreamCollector<T, ResultContainer_, Result_> {

    Supplier<ResultContainer_> supplier();

    BiConsumer<ResultContainer_, T> accumulator();

    BiConsumer<ResultContainer_, T> disperser();

    Function<ResultContainer_, Result_> finisher();

}
