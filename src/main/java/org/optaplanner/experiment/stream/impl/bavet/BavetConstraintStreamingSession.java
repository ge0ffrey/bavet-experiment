package org.optaplanner.experiment.stream.impl.bavet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.optaplanner.experiment.stream.impl.ConstraintStreamingSession;
import org.optaplanner.experiment.stream.impl.bavet.uni.BavetSelectConstraintStream;
import org.optaplanner.experiment.stream.impl.bavet.uni.BavetSelectConstraintStreaming;

public class BavetConstraintStreamingSession implements ConstraintStreamingSession {

    private final BavetConstraintStreamFactory factory;
    private final BavetSelectConstraintStreaming[] streamings;

    private long score = 0L;

    public BavetConstraintStreamingSession(BavetConstraintStreamFactory factory, List<BavetSelectConstraintStream<?>> streamList) {
        this.factory = factory;
        Map<Object, Object> mergeLinkMap = new HashMap<>(streamList.size() * 3);
        streamings = streamList.stream()
                .map((BavetSelectConstraintStream<?> stream) -> stream.buildStreaming(this, mergeLinkMap))
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




    public void insertScore(long addition) {
        score += addition;
    }

    public void retractScore(long addition) {
        score += addition;
    }

    @Override
    public long calculateScore() {
        return score;
    }

}
