package org.optaplanner.experiment.stream.api.bi;

import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;

public interface BiConstraintStream<A, B> {

    BiConstraintStream<A, B> filter(BiPredicate<A, B> predicate);

    void scoreEachMatch(String constraintName);

    /**
     * @param constraintName never null
     * @param matchWeighter never null, the result of this function (matchWeight) is multiplied by the constraintWeight
     */
    void scoreEachMatch(String constraintName, BiFunction<A, B, Long> matchWeighter);

}
