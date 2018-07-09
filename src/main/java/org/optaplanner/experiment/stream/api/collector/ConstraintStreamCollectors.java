package org.optaplanner.experiment.stream.api.collector;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

public class ConstraintStreamCollectors {

    private ConstraintStreamCollectors() {
    }

    public static <T> ConstraintStreamCollector<T, ?, Long> counting() {
        return new ConstraintStreamCollectorImpl<>(
                () -> new long[1],
                (resultContainer, t) -> resultContainer[0]++,
                (resultContainer, t) -> resultContainer[0]--,
                resultContainer -> resultContainer[0]);
    }


    public static <T> ConstraintStreamCollector<T, ?, Integer> summingInt(ToIntFunction<? super T> mapper) {
        return new ConstraintStreamCollectorImpl<>(
                () -> new int[1],
                (resultContainer, t) -> resultContainer[0] += mapper.applyAsInt(t),
                (resultContainer, t) -> resultContainer[0] -= mapper.applyAsInt(t),
                resultContainer -> resultContainer[0]);
    }


    public static <T> ConstraintStreamCollector<T, ?, Long> summingLong(ToLongFunction<? super T> mapper) {
        return new ConstraintStreamCollectorImpl<>(
                () -> new long[1],
                (resultContainer, t) -> resultContainer[0] += mapper.applyAsLong(t),
                (resultContainer, t) -> resultContainer[0] -= mapper.applyAsLong(t),
                resultContainer -> resultContainer[0]);
    }

    static class ConstraintStreamCollectorImpl<T, ResultContainer_, Result_>
            implements ConstraintStreamCollector<T, ResultContainer_, Result_> {

        private final Supplier<ResultContainer_> supplier;
        private final BiConsumer<ResultContainer_, T> accumulator;
        private final BiConsumer<ResultContainer_, T> disperser;
        private final Function<ResultContainer_, Result_> finisher;

        ConstraintStreamCollectorImpl(Supplier<ResultContainer_> supplier,
                BiConsumer<ResultContainer_, T> accumulator,
                BiConsumer<ResultContainer_, T> disperser,
                Function<ResultContainer_, Result_> finisher) {
            this.supplier = supplier;
            this.accumulator = accumulator;
            this.disperser = disperser;
            this.finisher = finisher;
        }

        @Override
        public Supplier<ResultContainer_> supplier() {
            return supplier;
        }

        @Override
        public BiConsumer<ResultContainer_, T> accumulator() {
            return accumulator;
        }

        @Override
        public BiConsumer<ResultContainer_, T> disperser() {
            return disperser;
        }

        @Override
        public Function<ResultContainer_, Result_> finisher() {
            return finisher;
        }

    }

}
