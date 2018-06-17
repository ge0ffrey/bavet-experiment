package org.optaplanner.experiment.stream.impl.bavet.uni;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import org.optaplanner.experiment.stream.api.bi.BiConstraintStream;
import org.optaplanner.experiment.stream.api.bi.BiJoiner;
import org.optaplanner.experiment.stream.impl.bavet.BavetConstraintStreamFactory;
import org.optaplanner.experiment.stream.impl.bavet.BavetConstraintStreamingSession;
import org.optaplanner.experiment.stream.api.uni.ConstraintStream;
import org.optaplanner.experiment.stream.impl.bavet.bi.BavetJoinBiConstraintStream;

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
    public <B, R> BiConstraintStream<A, B> join(ConstraintStream<B> other, BiJoiner<A, B, R> joiner) {
        if (!(other instanceof BavetConstraintStream)) {
            throw new IllegalStateException("The streams (" + this + ", " + other + ") must be build with the same factory.");
        }
        BavetConstraintStream<B> otherStream = (BavetConstraintStream<B>) other;
        if (factory != ((BavetConstraintStream<B>) other).factory) {
            throw new IllegalStateException("The streams (" + this + ", " + other + ") must be build with the same factory.");
        }
        BavetJoinBiConstraintStream<A, B, R> biStream = new BavetJoinBiConstraintStream<>(factory);
        BavetJoinLeftBridgeConstraintStream<A, B, R> leftBridge = new BavetJoinLeftBridgeConstraintStream<>(
                factory, biStream, joiner.getLeftMapping());
        nextStreamList.add(leftBridge);
        BavetJoinRightBridgeConstraintStream<A, B, R> rightBridge = new BavetJoinRightBridgeConstraintStream<>(
                factory, biStream, joiner.getRightMapping());
        otherStream.nextStreamList.add(rightBridge);
        return biStream;
    }

    @Override
    public void scoreEachMatch(String constraintName) {
        BavetScoringConstraintStream<A> stream = new BavetScoringConstraintStream<>(factory, constraintName);
        nextStreamList.add(stream);
    }

    public BavetConstraintStreaming<A> buildStreaming(BavetConstraintStreamingSession session, Map<Object, Object> mergeLinkMap) {
        if (nextStreamList.isEmpty()) {
            return buildStreamingToNext(session, mergeLinkMap, null);
        } else if (nextStreamList.size() == 1) {
            BavetConstraintStream<A> nextStream = nextStreamList.get(0);
            return buildStreamingToNext(session, mergeLinkMap, nextStream.buildStreaming(session, mergeLinkMap));
        }
        throw new UnsupportedOperationException(); // TODO support multiple next streams
    }

    protected abstract BavetConstraintStreaming<A> buildStreamingToNext(
            BavetConstraintStreamingSession session, Map<Object, Object> mergeLinkMap,
            BavetConstraintStreaming<A> nextStreaming);

}
