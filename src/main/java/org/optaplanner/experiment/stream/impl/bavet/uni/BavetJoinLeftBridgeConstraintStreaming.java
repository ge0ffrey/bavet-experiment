package org.optaplanner.experiment.stream.impl.bavet.uni;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.optaplanner.experiment.stream.impl.bavet.bi.BavetJoinBiConstraintStreaming;

public class BavetJoinLeftBridgeConstraintStreaming<A, B, R> extends BavetConstraintStreaming<A> {

    private BavetJoinBiConstraintStreaming<A, B, R> biStreaming;
    private final Function<A, R> mapping;

    private final Map<R, List<A>> index;

    public BavetJoinLeftBridgeConstraintStreaming(BavetJoinBiConstraintStreaming<A, B, R> biStreaming, Function<A, R> mapping) {
        this.biStreaming = biStreaming;
        this.mapping = mapping;
        index = new HashMap<>();
    }

    @Override
    public void insert(A a) {
        R indexKey = mapping.apply(a);
        boolean added = index.computeIfAbsent(indexKey, k -> new ArrayList<>()).add(a);
        if (!added) {
            throw new IllegalStateException("Impossible state: the fact (" + a + ") with indexKey (" + indexKey
                    + ") cannot be added to the index (" + index.keySet() + ").");
        }
        biStreaming.insertLeft(a, indexKey);
    }

    public void retract(A a) {
        R indexKey = mapping.apply(a);
        List<A> indexValueList = index.get(indexKey);
        boolean removed = indexValueList.remove(a);
        if (!removed) {
            throw new IllegalStateException("Impossible state: the fact (" + a + ") with indexKey (" + indexKey
                    + ") cannot be removed from the index (" + index.keySet() + ").");
        }
        if (indexValueList.isEmpty()) {
            index.remove(indexKey);
        }
        biStreaming.retractLeft(a, indexKey);
    }

    public Function<A, R> getMapping() {
        return mapping;
    }

    public List<A> getIndexValueList(R indexKey) {
        return index.get(indexKey);
    }

}
