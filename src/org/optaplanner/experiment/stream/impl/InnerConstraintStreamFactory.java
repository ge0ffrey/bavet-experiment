package org.optaplanner.experiment.stream.impl;

import org.optaplanner.experiment.stream.ConstraintStreamFactory;

public interface InnerConstraintStreamFactory extends ConstraintStreamFactory {

    ConstraintStreamingSession buildSession();

}
