package org.optaplanner.experiment.stream.impl.bavet.uni;

import java.util.Map;
import java.util.function.Function;

import org.optaplanner.experiment.stream.impl.bavet.BavetConstraintStreamFactory;
import org.optaplanner.experiment.stream.impl.bavet.BavetConstraintStreamingSession;
import org.optaplanner.experiment.stream.impl.bavet.bi.BavetBiConstraintStreaming;
import org.optaplanner.experiment.stream.impl.bavet.bi.BavetJoinBiConstraintStream;
import org.optaplanner.experiment.stream.impl.bavet.bi.BavetJoinBiConstraintStreaming;

public class BavetJoinLeftBridgeConstraintStream<A, B, R> extends BavetConstraintStream<A> {

    private final BavetJoinBiConstraintStream<A, B, R> biStream;
    private final Function<A, R> mapping;

    public BavetJoinLeftBridgeConstraintStream(BavetConstraintStreamFactory factory,
            BavetJoinBiConstraintStream<A, B, R> biStream, Function<A, R> mapping) {
        super(factory);
        this.biStream = biStream;
        this.mapping = mapping;
    }

    @Override
    protected BavetJoinBridgeConstraintStreaming<A, R> buildStreamingToNext(
            BavetConstraintStreamingSession session, Map<Object, Object> mergeLinkMap,
            BavetConstraintStreaming<A> nextStreaming) {
        if (nextStreaming != null) {
            throw new IllegalStateException("Impossible state: the stream (" + this + ") has one ore more nextStreams ("
                    + nextStreamList + ") but it's a join bridge.");
        }
        BavetJoinBiConstraintStreaming<A, B, R> biStreaming = (BavetJoinBiConstraintStreaming<A, B, R>) mergeLinkMap.get(biStream);
        if (biStreaming == null) {
            biStreaming = (BavetJoinBiConstraintStreaming<A, B, R>) biStream.buildStreaming(session, mergeLinkMap);
            mergeLinkMap.put(biStream, biStreaming);
        }
        BavetJoinBridgeConstraintStreaming<A, R> stream = new BavetJoinBridgeConstraintStreaming<>(
                biStreaming::insertLeft, biStreaming::retractLeft, mapping);
        biStreaming.setLeftParentStreaming(stream);
        return stream;
    }

}
