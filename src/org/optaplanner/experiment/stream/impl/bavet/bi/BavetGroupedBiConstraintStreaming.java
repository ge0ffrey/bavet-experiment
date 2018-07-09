package org.optaplanner.experiment.stream.impl.bavet.bi;

import java.util.List;

import org.optaplanner.experiment.stream.impl.bavet.uni.BavetJoinLeftBridgeConstraintStreaming;
import org.optaplanner.experiment.stream.impl.bavet.uni.BavetJoinRightBridgeConstraintStreaming;

public class BavetGroupedBiConstraintStreaming<GroupKey_, Result_> extends BavetBiConstraintStreaming<GroupKey_, Result_> {

    private final BavetBiConstraintStreaming<GroupKey_, Result_> nextStreaming;

    public BavetGroupedBiConstraintStreaming(BavetBiConstraintStreaming<GroupKey_, Result_> nextStreaming) {
        this.nextStreaming = nextStreaming;
    }

    @Override
    public void insert(GroupKey_ a, Result_ b) {
        nextStreaming.insert(a, b);
    }

    @Override
    public void retract(GroupKey_ a, Result_ b) {
        nextStreaming.retract(a, b);
    }

}
