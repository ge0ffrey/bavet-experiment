package org.optaplanner.experiment.bavet;

import java.util.ArrayList;
import java.util.List;

public class BavetFactory {

    private List<BavetSelectStream<Object>> streamList = new ArrayList<>();
    private long score = 0L;

    public <A> BavetStream<A> select(Class<A> selectClass) {
        BavetSelectStream<A> stream = new BavetSelectStream<>(this, selectClass);
        streamList.add((BavetSelectStream<Object>) stream); // TODO
        return stream;
    }

    public void insert(Object fact) {
        for (BavetStream<Object> stream : streamList) {
            stream.insert(fact);
        }
    }

    public void retract(Object fact) {
        for (BavetStream<Object> stream : streamList) {
            stream.retract(fact);
        }
    }

    public void adjustScore(long diff) {
        score += diff;
    }

    public long calculateScore() {
        return score;
    }

}
