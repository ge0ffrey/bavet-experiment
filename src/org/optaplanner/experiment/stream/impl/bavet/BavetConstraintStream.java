package org.optaplanner.experiment.stream.impl.bavet;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

import org.optaplanner.experiment.stream.BiConstraintStream;
import org.optaplanner.experiment.stream.ConstraintStream;

public abstract class BavetConstraintStream<A> implements ConstraintStream<A> {

    protected final BavetConstraintStreamFactory factory;
    protected final List<BavetConstraintStream<A>> nextStreamList = new ArrayList<>(2);

    public BavetConstraintStream(BavetConstraintStreamFactory factory) {
        this.factory = factory;
    }

    @Override
    public ConstraintStream<A> filter(Predicate<A> predicate) {
        BavetFilterConstraintStream<A> stream = new BavetFilterConstraintStream<>(factory, predicate);
        nextStreamList.add(stream);
        return stream;
    }

    @Override
    public <B> BiConstraintStream<A, B> join(ConstraintStream<B> other, BiPredicate<A, B> predicate) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    public void scoreEachMatch(String constraintName) {
        BavetScoringConstraintStream<A> stream = new BavetScoringConstraintStream<>(factory, constraintName);
        nextStreamList.add(stream);
    }

    public BavetConstraintStreaming<A> buildStreaming(BavetConstraintStreamingSession session) {
        if (nextStreamList.isEmpty()) {
            return buildStreamingToNext(session, null);
        } else if (nextStreamList.size() == 1) {
            BavetConstraintStream<A> nextStream = nextStreamList.get(0);
            return buildStreamingToNext(session, nextStream.buildStreaming(session));
        }
        throw new UnsupportedOperationException(); // TODO support multiple next streams
    }

    protected abstract BavetConstraintStreaming<A> buildStreamingToNext(
            BavetConstraintStreamingSession session, BavetConstraintStreaming<A> nextStreaming);

}
