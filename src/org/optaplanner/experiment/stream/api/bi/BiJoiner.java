package org.optaplanner.experiment.stream.api.bi;

import java.util.function.Function;

public class BiJoiner<A, B, C> {

    private final Function<A, C> aMapping;
    private final Function<B, C> bMapping;

    public BiJoiner(Function<A, C> aMapping, Function<B, C> bMapping) {
        this.aMapping = aMapping;
        this.bMapping = bMapping;
    }

    public static <A, C> BiJoiner<A, A, C> equals(Function<A, C> mapping) {
        return equals(mapping, mapping);
    }

    public static <A, B, C> BiJoiner<A, B, C> equals(Function<A, C> aMapping, Function <B, C> bMapping) {
        return new BiJoiner<>(aMapping, bMapping);
    }

}
