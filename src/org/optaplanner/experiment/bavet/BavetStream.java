package org.optaplanner.experiment.bavet;

import java.util.function.BiPredicate;
import java.util.function.Predicate;

public interface BavetStream<A> { // Rename to ConstraintStream

    default BavetStream<A> filter(Predicate<A> predicate) {
        BavetFilterStream<A> filterStream = new BavetFilterStream<>(getFactory(), predicate);
        setNextStream(filterStream);
        return filterStream;
    }

    default BavetEndpoint<A> forEachMatch(String constraintName) {
        BavetEndpoint<A> endpoint = new BavetEndpoint<>(getFactory(), constraintName);
        setNextStream(endpoint);
        return endpoint;
    }

    default <B> BiBavetStream<A, B> join(BavetStream<B> other, BiPredicate<A, B> predicate) {
        BiBavetJoinStream<A, B> joinStream = new BiBavetJoinStream<>(getFactory(), predicate);
        setNextStream(joinStream);
        other.setNextStream(joinStream);
        return joinStream;
    }

    BavetFactory getFactory();

    void setNextStream(BavetStream<A> nextStream);

    void insert(A fact);

    void retract(A fact);

}
