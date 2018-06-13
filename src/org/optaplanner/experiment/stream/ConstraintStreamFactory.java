package org.optaplanner.experiment.stream;

public interface ConstraintStreamFactory {

    <A> ConstraintStream<A> select(Class<A> selectClass);

}
