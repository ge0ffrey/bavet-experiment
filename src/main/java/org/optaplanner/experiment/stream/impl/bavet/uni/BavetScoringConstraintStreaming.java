package org.optaplanner.experiment.stream.impl.bavet.uni;

import java.util.function.Function;

import org.optaplanner.experiment.stream.impl.bavet.BavetConstraintStreamingSession;

public class BavetScoringConstraintStreaming<A> extends BavetConstraintStreaming<A> {

    private final String constraintName;
    private final Function<A, Long> matchWeighter;
    private final BavetConstraintStreamingSession session;

    public BavetScoringConstraintStreaming(String constraintName, Function<A, Long> matchWeighter, BavetConstraintStreamingSession session) {
        this.constraintName = constraintName;
        this.matchWeighter = matchWeighter;
        this.session = session;
    }

    @Override
    public void insert(A a) {
        long matchWeight = matchWeighter.apply(a);
        session.insertScore(-1L * matchWeight);
    }

    public void retract(A a) {
        long matchWeight = matchWeighter.apply(a);
        session.retractScore(1L * matchWeight);
    }

}
