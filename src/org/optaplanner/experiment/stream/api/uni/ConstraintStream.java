package org.optaplanner.experiment.stream.api.uni;

import java.util.function.Predicate;

import org.optaplanner.experiment.stream.api.bi.BiConstraintStream;
import org.optaplanner.experiment.stream.api.bi.BiJoiner;

public interface ConstraintStream<A> {

    ConstraintStream<A> filter(Predicate<A> predicate);

    <B, C> BiConstraintStream<A, B> join(ConstraintStream<B> other, BiJoiner<A, B, C> joiner);

    void scoreEachMatch(String constraintName);

}
