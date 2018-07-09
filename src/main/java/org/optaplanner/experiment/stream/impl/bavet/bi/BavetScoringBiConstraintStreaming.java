package org.optaplanner.experiment.stream.impl.bavet.bi;

import java.util.function.BiFunction;

import org.optaplanner.experiment.stream.impl.bavet.BavetConstraintStreamingSession;

public class BavetScoringBiConstraintStreaming<A, B> extends BavetBiConstraintStreaming<A, B> {

    private final String constraintName;
    private final BiFunction<A, B, Long> matchWeighter;
    private final BavetConstraintStreamingSession session;

    public BavetScoringBiConstraintStreaming(String constraintName, BiFunction<A, B, Long> matchWeighter,
            BavetConstraintStreamingSession session) {
        this.constraintName = constraintName;
        this.matchWeighter = matchWeighter;
        this.session = session;
    }

    @Override
    public void insert(A a, B b) {
        long matchWeight = matchWeighter.apply(a, b);
        session.insertScore(-1L * matchWeight);
    }

    public void retract(A a, B b) {
        long matchWeight = matchWeighter.apply(a, b);
        session.retractScore(1L * matchWeight);
    }

}
