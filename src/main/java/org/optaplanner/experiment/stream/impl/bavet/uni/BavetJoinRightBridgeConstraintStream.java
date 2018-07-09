package org.optaplanner.experiment.stream.impl.bavet.uni;

import java.util.Map;
import java.util.function.Function;

import org.optaplanner.experiment.stream.impl.bavet.BavetConstraintStreamFactory;
import org.optaplanner.experiment.stream.impl.bavet.BavetConstraintStreamingSession;
import org.optaplanner.experiment.stream.impl.bavet.bi.BavetJoinBiConstraintStream;
import org.optaplanner.experiment.stream.impl.bavet.bi.BavetJoinBiConstraintStreaming;

public class BavetJoinRightBridgeConstraintStream<A, B, R> extends BavetConstraintStream<B> {

    private final BavetJoinBiConstraintStream<A, B, R> biStream;
    private final Function<B, R> mapping;

    public BavetJoinRightBridgeConstraintStream(BavetConstraintStreamFactory factory,
            BavetJoinBiConstraintStream<A, B, R> biStream, Function<B, R> mapping) {
        super(factory);
        this.biStream = biStream;
        this.mapping = mapping;
    }

    @Override
    protected BavetJoinRightBridgeConstraintStreaming<A, B, R> buildStreamingToNext(
            BavetConstraintStreamingSession session, Map<Object, Object> mergeLinkMap,
            BavetConstraintStreaming<B> nextStreaming) {
        if (nextStreaming != null) {
            throw new IllegalStateException("Impossible state: the stream (" + this + ") has nextStreams ("
                    + nextStreamList + ") but it's a bridge.");
        }
        BavetJoinBiConstraintStreaming<A, B, R> biStreaming = (BavetJoinBiConstraintStreaming<A, B, R>) mergeLinkMap.get(biStream);
        if (biStreaming == null) {
            biStreaming = (BavetJoinBiConstraintStreaming<A, B, R>) biStream.buildStreaming(session, mergeLinkMap);
            mergeLinkMap.put(biStream, biStreaming);
        }
        BavetJoinRightBridgeConstraintStreaming<A, B, R> streaming = new BavetJoinRightBridgeConstraintStreaming<>(
                biStreaming, mapping);
        biStreaming.setRightParentStreaming(streaming);
        return streaming;
    }

}
