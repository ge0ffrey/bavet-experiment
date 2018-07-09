package org.optaplanner.experiment.stream.api.uni;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;

import org.optaplanner.experiment.stream.api.bi.BiConstraintStream;
import org.optaplanner.experiment.stream.api.bi.BiJoiner;
import org.optaplanner.experiment.stream.api.collector.ConstraintStreamCollector;

public interface ConstraintStream<A> {

    ConstraintStream<A> filter(Predicate<A> predicate);

    <B, R> BiConstraintStream<A, B> join(ConstraintStream<B> other, BiJoiner<A, B, R> joiner);

    <GroupKey_, ResultContainer_, Result_> BiConstraintStream<GroupKey_, Result_> groupBy(Function<A, GroupKey_> mapping,
            ConstraintStreamCollector<A, ResultContainer_, Result_> collector);

    void scoreEachMatch(String constraintName);

    /**
     * @param constraintName never null
     * @param matchWeighter never null, the result of this function (matchWeight) is multiplied by the constraintWeight
     */
    void scoreEachMatch(String constraintName, Function<A, Long> matchWeighter);

}
