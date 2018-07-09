package org.optaplanner.experiment.stream.impl.bavet.bi;

import java.util.List;

import org.optaplanner.experiment.stream.impl.bavet.uni.BavetJoinLeftBridgeConstraintStreaming;
import org.optaplanner.experiment.stream.impl.bavet.uni.BavetJoinRightBridgeConstraintStreaming;

public class BavetJoinBiConstraintStreaming<A, B, R> extends BavetBiConstraintStreaming<A, B> {

    private BavetJoinLeftBridgeConstraintStreaming<A, B, R> leftParentStreaming;
    private BavetJoinRightBridgeConstraintStreaming<A, B, R> rightParentStreaming;
    private final BavetBiConstraintStreaming<A, B> nextStreaming;

    public BavetJoinBiConstraintStreaming(BavetBiConstraintStreaming<A, B> nextStreaming) {
        this.nextStreaming = nextStreaming;
    }

    public void setLeftParentStreaming(BavetJoinLeftBridgeConstraintStreaming<A, B, R> leftParentStreaming) {
        this.leftParentStreaming = leftParentStreaming;
    }

    public void setRightParentStreaming(BavetJoinRightBridgeConstraintStreaming<A, B, R> rightParentStreaming) {
        this.rightParentStreaming = rightParentStreaming;
    }

    @Override
    public void insert(A a, B b) {
        nextStreaming.insert(a, b);
    }

    @Override
    public void retract(A a, B b) {
        nextStreaming.retract(a, b);
    }

    public void insertLeft(A a, R indexKey) {
        List<B> indexValueList = rightParentStreaming.getIndexValueList(indexKey);
        if (indexValueList != null) {
            indexValueList.forEach(b -> insert(a, b));
        }
    }

    public void retractLeft(A a, R indexKey) {
        List<B> indexValueList = rightParentStreaming.getIndexValueList(indexKey);
        if (indexValueList != null) {
            indexValueList.forEach(b -> retract(a, b));
        }
    }

    public void insertRight(B b, R indexKey) {
        List<A> indexValueList = leftParentStreaming.getIndexValueList(indexKey);
        if (indexValueList != null) {
            indexValueList.forEach(a -> insert(a, b));
        }
    }

    public void retractRight(B b, R indexKey) {
        List<A> indexValueList = leftParentStreaming.getIndexValueList(indexKey);
        if (indexValueList != null) {
            indexValueList.forEach(a -> retract(a, b));
        }
    }

}
