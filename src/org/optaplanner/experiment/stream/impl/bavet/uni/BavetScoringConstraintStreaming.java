package org.optaplanner.experiment.stream.impl.bavet.uni;

import org.optaplanner.experiment.stream.impl.bavet.BavetConstraintStreamingSession;

public class BavetScoringConstraintStreaming<A> extends BavetConstraintStreaming<A> {

    private final String constraintName;
    private final BavetConstraintStreamingSession session;

    public BavetScoringConstraintStreaming(String constraintName, BavetConstraintStreamingSession session) {
        this.constraintName = constraintName;
        this.session = session;
    }

    @Override
    public void insert(A fact) {
        session.removeScore(1L);
    }

    public void retract(A fact) {
        session.addScore(1L);
    }

}
