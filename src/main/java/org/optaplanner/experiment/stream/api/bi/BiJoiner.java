package org.optaplanner.experiment.stream.api.bi;

import java.util.function.Function;

public class BiJoiner<A, B, C> {

    public static <A, C> BiJoiner<A, A, C> equals(Function<A, C> mapping) {
        return equals(mapping, mapping);
    }

    public static <A, B, C> BiJoiner<A, B, C> equals(Function<A, C> leftMapping, Function <B, C> rightMapping) {
        return new BiJoiner<>(leftMapping, rightMapping);
    }

    private final Function<A, C> leftMapping;
    private final Function<B, C> bMapping;

    public BiJoiner(Function<A, C> leftMapping, Function<B, C> rightMapping) {
        this.leftMapping = leftMapping;
        this.bMapping = rightMapping;
    }

    public Function<A, C> getLeftMapping() {
        return leftMapping;
    }

    public Function<B, C> getRightMapping() {
        return bMapping;
    }

}
