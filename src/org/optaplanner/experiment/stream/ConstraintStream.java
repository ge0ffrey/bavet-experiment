package org.optaplanner.experiment.stream;

import java.util.function.BiPredicate;
import java.util.function.Predicate;


public interface ConstraintStream<A> {

    ConstraintStream<A> filter(Predicate<A> predicate);

    <B> BiConstraintStream<A, B> join(ConstraintStream<B> other, BiPredicate<A, B> predicate);

    void scoreEachMatch(String constraintName);

}
