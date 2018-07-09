package org.optaplanner.experiment.stream.impl.bavet.bi;

import org.optaplanner.experiment.stream.impl.bavet.BavetConstraintStreamingSession;
import org.optaplanner.experiment.stream.impl.bavet.uni.BavetConstraintStreaming;

public class BavetScoringBiConstraintStreaming<A, B> extends BavetBiConstraintStreaming<A, B> {

    private final String constraintName;
    private final BavetConstraintStreamingSession session;

    public BavetScoringBiConstraintStreaming(String constraintName, BavetConstraintStreamingSession session) {
        this.constraintName = constraintName;
        this.session = session;
    }

    @Override
    public void insert(A a, B b) {
        session.removeScore(1L);
    }

    public void retract(A a, B b) {
        session.addScore(1L);
    }

}
