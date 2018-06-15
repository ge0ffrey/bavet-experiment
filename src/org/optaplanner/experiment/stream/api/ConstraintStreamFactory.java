package org.optaplanner.experiment.stream.api;

import org.optaplanner.experiment.stream.api.uni.ConstraintStream;

public interface ConstraintStreamFactory {

    <A> ConstraintStream<A> select(Class<A> selectClass);

}
