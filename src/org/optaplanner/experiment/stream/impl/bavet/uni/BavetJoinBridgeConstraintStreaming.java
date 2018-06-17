package org.optaplanner.experiment.stream.impl.bavet.uni;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public class BavetJoinBridgeConstraintStreaming<A, R> extends BavetConstraintStreaming<A> {

    private final Consumer<A> nextStreamingInserter;
    private final Consumer<A> nextStreamingRetracter;
    private final Function<A, R> mapping;
    private final Map<R, List<A>> index;

    public BavetJoinBridgeConstraintStreaming(Consumer<A> nextStreamingInserter, Consumer<A> nextStreamingRetracter, Function<A, R> mapping) {
        this.nextStreamingInserter = nextStreamingInserter;
        this.nextStreamingRetracter = nextStreamingRetracter;
        this.mapping = mapping;
        index = new HashMap<>();
    }

    @Override
    public void insert(A a) {
        R indexKey = mapping.apply(a);
        boolean added = index.computeIfAbsent(indexKey, k -> new ArrayList<>()).add(a);
        if (!added) {
            throw new IllegalStateException("Impossible situation: the fact (" + a + ") with indexKey (" + indexKey
                    + ") could not be added to the index (" + index.keySet() + ").");
        }
        nextStreamingInserter.accept(a);
    }

    public void retract(A a) {
        R indexKey = mapping.apply(a);
        List<A> indexValueList = index.get(indexKey);
        boolean removed = indexValueList.remove(a);
        if (!removed) {
            throw new IllegalStateException("Impossible situation: the fact (" + a + ") with indexKey (" + indexKey
                    + ") could not removed from the index (" + index.keySet() + ").");
        }
        if (indexValueList.isEmpty()) {
            index.remove(indexKey);
        }
        nextStreamingRetracter.accept(a);
    }

    public Function<A, R> getMapping() {
        return mapping;
    }

    public List<A> getIndexValueList(R r) {
        return index.get(r);
    }

}
