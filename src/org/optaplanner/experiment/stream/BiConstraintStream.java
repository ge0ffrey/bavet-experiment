package org.optaplanner.experiment.stream;

import java.util.function.BiPredicate;

public interface BiConstraintStream<A, B> {

    BiConstraintStream<A, B> filter(BiPredicate<A, B> predicate);

    void forEachMatch(String constraintName);

}
