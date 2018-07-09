package org.optaplanner.experiment.stream.impl.bavet.uni;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.optaplanner.experiment.stream.api.collector.ConstraintStreamCollector;
import org.optaplanner.experiment.stream.impl.bavet.bi.BavetGroupedBiConstraintStreaming;

public class BavetGroupByBridgeConstraintStreaming<A, GroupKey_, ResultContainer_, Result_>
        extends BavetConstraintStreaming<A> {

    private final BavetGroupedBiConstraintStreaming<GroupKey_, Result_> biStreaming;
    private final Function<A, GroupKey_> groupKeyMapping;
    private final ConstraintStreamCollector<A, ResultContainer_, Result_> collector;

    private final Map<GroupKey_, ResultContainer_> resultContainerMap;

    public BavetGroupByBridgeConstraintStreaming(BavetGroupedBiConstraintStreaming<GroupKey_, Result_> biStreaming,
            Function<A, GroupKey_> groupKeyMapping, ConstraintStreamCollector<A, ResultContainer_, Result_> collector) {
        this.biStreaming = biStreaming;
        this.groupKeyMapping = groupKeyMapping;
        this.collector = collector;
        resultContainerMap = new HashMap<>();
    }

    @Override
    public void insert(A a) {
        GroupKey_ groupKey = groupKeyMapping.apply(a);

        ResultContainer_ resultContainer = resultContainerMap.get(groupKey);
        if (resultContainer == null) {
            resultContainer = collector.supplier().get();
            resultContainerMap.put(groupKey, resultContainer);
        } else {
            biStreaming.retract(groupKey, collector.finisher().apply(resultContainer));
        }
        collector.accumulator().accept(resultContainer, a); // TODO should return boolean if changed. retract only happens if it changed
        biStreaming.insert(groupKey, collector.finisher().apply(resultContainer));
    }

    public void retract(A a) {
        GroupKey_ groupKey = groupKeyMapping.apply(a);

        ResultContainer_ resultContainer = resultContainerMap.get(groupKey);
        if (resultContainer == null) {
            throw new IllegalStateException("Impossible state: the groupKey (" + groupKey + ") of fact (" + a
                    + ") is not in the resultContainerMap.");
        } else {
            biStreaming.retract(groupKey, collector.finisher().apply(resultContainer));
        }
        collector.disperser().accept(resultContainer, a); // TODO should return boolean if changed. retract only happens if it changed
        biStreaming.insert(groupKey, collector.finisher().apply(resultContainer));
    }

}
