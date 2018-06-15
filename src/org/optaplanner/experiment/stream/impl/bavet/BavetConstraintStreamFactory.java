package org.optaplanner.experiment.stream.impl.bavet;

import java.util.ArrayList;
import java.util.List;

import org.optaplanner.experiment.stream.impl.bavet.uni.BavetSelectConstraintStream;
import org.optaplanner.experiment.stream.api.uni.ConstraintStream;
import org.optaplanner.experiment.stream.impl.ConstraintStreamingSession;
import org.optaplanner.experiment.stream.impl.InnerConstraintStreamFactory;

public class BavetConstraintStreamFactory implements InnerConstraintStreamFactory {

    private List<BavetSelectConstraintStream<?>> streamList = new ArrayList<>();

    @Override
    public <A> ConstraintStream<A> select(Class<A> selectClass) {
        BavetSelectConstraintStream<A> stream = new BavetSelectConstraintStream<>(this, selectClass);
        streamList.add(stream);
        return stream;
    }

    @Override
    public ConstraintStreamingSession buildSession() {
        return new BavetConstraintStreamingSession(this, streamList);
    }

}
