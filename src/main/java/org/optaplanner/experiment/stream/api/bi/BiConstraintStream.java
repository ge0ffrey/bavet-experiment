package org.optaplanner.experiment.stream.api.bi;

import java.util.function.BiPredicate;
import java.util.function.Function;

public interface BiConstraintStream<A, B> {

    BiConstraintStream<A, B> filter(BiPredicate<A, B> predicate);

    void scoreEachMatch(String constraintName);

}
