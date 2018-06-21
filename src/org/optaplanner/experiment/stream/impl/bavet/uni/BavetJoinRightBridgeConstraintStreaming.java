package org.optaplanner.experiment.stream.impl.bavet.uni;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.optaplanner.experiment.stream.impl.bavet.bi.BavetJoinBiConstraintStreaming;

public class BavetJoinRightBridgeConstraintStreaming<A, B, R> extends BavetConstraintStreaming<B> {

    private BavetJoinBiConstraintStreaming<A, B, R> biStreaming;
    private final Function<B, R> mapping;
    private final Map<R, List<B>> index;

    public BavetJoinRightBridgeConstraintStreaming(BavetJoinBiConstraintStreaming<A, B, R> biStreaming, Function<B, R> mapping) {
        this.biStreaming = biStreaming;
        this.mapping = mapping;
        index = new HashMap<>();
    }

    @Override
    public void insert(B b) {
        R indexKey = mapping.apply(b);
        boolean added = index.computeIfAbsent(indexKey, k -> new ArrayList<>()).add(b);
        if (!added) {
            throw new IllegalStateException("Impossible situation: the fact (" + b + ") with indexKey (" + indexKey
                    + ") could not be added to the index (" + index.keySet() + ").");
        }
        biStreaming.insertRight(b, indexKey);
    }

    public void retract(B b) {
        R indexKey = mapping.apply(b);
        List<B> indexValueList = index.get(indexKey);
        boolean removed = indexValueList.remove(b);
        if (!removed) {
            throw new IllegalStateException("Impossible situation: the fact (" + b + ") with indexKey (" + indexKey
                    + ") could not removed from the index (" + index.keySet() + ").");
        }
        if (indexValueList.isEmpty()) {
            index.remove(indexKey);
        }
        biStreaming.retractRight(b, indexKey);
    }

    public Function<B, R> getMapping() {
        return mapping;
    }

    public List<B> getIndexValueList(R indexKey) {
        return index.get(indexKey);
    }

}
