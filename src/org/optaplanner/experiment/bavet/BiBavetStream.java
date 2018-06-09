package org.optaplanner.experiment.bavet;

import java.util.function.BiPredicate;
import java.util.function.Predicate;

public interface BiBavetStream<A, B> {

    default BiBavetStream<A, B> filter(BiPredicate<A, B> predicate) {
        throw new UnsupportedOperationException(); // TODO
    }

    default BiBavetEndpoint<A, B> forEachMatch(String constraintName) {
        BiBavetEndpoint<A, B> endpoint = new BiBavetEndpoint<>(getFactory(), constraintName);
        setNextStream(endpoint);
        return endpoint;
    }

    BavetFactory getFactory();

    void setNextStream(BiBavetStream<A, B> nextStream);

    void insert(A a, B b);

    void retract(A a, B b);

}
