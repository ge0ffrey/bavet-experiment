package org.optaplanner.experiment.stream.impl.bavet;

import java.util.List;

import org.optaplanner.experiment.stream.impl.ConstraintStreamingSession;

public class BavetConstraintStreamingSession implements ConstraintStreamingSession {

    private final BavetConstraintStreamFactory factory;
    private final BavetSelectConstraintStreaming[] streamings;

    private long score = 0L;

    public BavetConstraintStreamingSession(BavetConstraintStreamFactory factory, List<BavetSelectConstraintStream<?>> streamList) {
        this.factory = factory;
        streamings = streamList.stream()
                .map((BavetSelectConstraintStream<?> stream) -> stream.buildStreaming(this))
                .toArray(BavetSelectConstraintStreaming[]::new);
    }

    @Override
    public void insert(Object fact) {
        for (BavetSelectConstraintStreaming streaming : streamings) {
            streaming.insert(fact);
        }
    }

    @Override
    public void retract(Object fact) {
        for (BavetSelectConstraintStreaming streaming : streamings) {
            streaming.retract(fact);
        }
    }




    public void addScore(long addition) {
        score += addition;
    }

    public void removeScore(long subtraction) {
        score -= subtraction;
    }

    @Override
    public long calculateScore() {
        return score;
    }

}
