package org.optaplanner.experiment.stream.impl.bavet.bi;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

import org.optaplanner.experiment.stream.api.bi.BiConstraintStream;
import org.optaplanner.experiment.stream.api.bi.BiJoiner;
import org.optaplanner.experiment.stream.api.uni.ConstraintStream;
import org.optaplanner.experiment.stream.impl.bavet.BavetConstraintStreamFactory;
import org.optaplanner.experiment.stream.impl.bavet.BavetConstraintStreamingSession;
import org.optaplanner.experiment.stream.impl.bavet.uni.BavetConstraintStreaming;
import org.optaplanner.experiment.stream.impl.bavet.uni.BavetFilterConstraintStream;
import org.optaplanner.experiment.stream.impl.bavet.uni.BavetScoringConstraintStream;

public abstract class BavetBiConstraintStream<A, B> implements BiConstraintStream<A, B> {

    protected final BavetConstraintStreamFactory factory;
    protected final List<BavetBiConstraintStream<A, B>> nextStreamList = new ArrayList<>(2);

    public BavetBiConstraintStream(BavetConstraintStreamFactory factory) {
        this.factory = factory;
    }

    @Override
    public BiConstraintStream<A, B> filter(BiPredicate<A, B> predicate) {
        BavetFilterBiConstraintStream<A, B> stream = new BavetFilterBiConstraintStream<>(factory, predicate);
        nextStreamList.add(stream);
        return stream;
    }

    @Override
    public void scoreEachMatch(String constraintName) {
        BavetScoringBiConstraintStream<A, B> stream = new BavetScoringBiConstraintStream<>(factory, constraintName);
        nextStreamList.add(stream);
    }

    public BavetBiConstraintStreaming<A, B> buildStreaming(
            BavetConstraintStreamingSession session, Map<Object, Object> mergeLinkMap) {
        if (nextStreamList.isEmpty()) {
            return buildStreamingToNext(session, mergeLinkMap, null);
        } else if (nextStreamList.size() == 1) {
            BavetBiConstraintStream<A, B> nextStream = nextStreamList.get(0);
            return buildStreamingToNext(session, mergeLinkMap, nextStream.buildStreaming(session, mergeLinkMap));
        }
        throw new UnsupportedOperationException(); // TODO support multiple next streams
    }

    protected abstract BavetBiConstraintStreaming<A, B> buildStreamingToNext(
            BavetConstraintStreamingSession session, Map<Object, Object> mergeLinkMap,
            BavetBiConstraintStreaming<A, B> nextStreaming);

}
