package org.optaplanner.experiment.stream.impl.bavet.uni;

import java.util.Map;
import java.util.function.Function;

import org.optaplanner.experiment.stream.api.collector.ConstraintStreamCollector;
import org.optaplanner.experiment.stream.impl.bavet.BavetConstraintStreamFactory;
import org.optaplanner.experiment.stream.impl.bavet.BavetConstraintStreamingSession;
import org.optaplanner.experiment.stream.impl.bavet.bi.BavetGroupedBiConstraintStream;
import org.optaplanner.experiment.stream.impl.bavet.bi.BavetGroupedBiConstraintStreaming;

public class BavetGroupByBridgeConstraintStream<A, GroupKey_, ResultContainer_, Result_>
        extends BavetConstraintStream<A> {

    private final BavetGroupedBiConstraintStream<GroupKey_, Result_> biStream;
    private final Function<A, GroupKey_> groupKeyMapping;
    private final ConstraintStreamCollector<A, ResultContainer_, Result_> collector;

    public BavetGroupByBridgeConstraintStream(BavetConstraintStreamFactory factory,
            BavetGroupedBiConstraintStream<GroupKey_, Result_> biStream, Function<A, GroupKey_> groupKeyMapping,
            ConstraintStreamCollector<A, ResultContainer_, Result_> collector) {
        super(factory);
        this.biStream = biStream;
        this.groupKeyMapping = groupKeyMapping;
        this.collector = collector;
    }

    @Override
    protected BavetGroupByBridgeConstraintStreaming<A, GroupKey_, ResultContainer_, Result_> buildStreamingToNext(
            BavetConstraintStreamingSession session, Map<Object, Object> mergeLinkMap,
            BavetConstraintStreaming<A> nextStreaming) {
        if (nextStreaming != null) {
            throw new IllegalStateException("Impossible state: the stream (" + this + ") has nextStreams ("
                    + nextStreamList + ") but it's a bridge.");
        }
        BavetGroupedBiConstraintStreaming<GroupKey_, Result_> biStreaming
                = (BavetGroupedBiConstraintStreaming<GroupKey_, Result_>) biStream.buildStreaming(session, mergeLinkMap);

        return new BavetGroupByBridgeConstraintStreaming<>(biStreaming, groupKeyMapping, collector);
    }

}
