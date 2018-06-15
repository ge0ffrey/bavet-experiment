package org.optaplanner.experiment.stream.impl;

import org.optaplanner.experiment.stream.api.ConstraintStreamFactory;

public interface InnerConstraintStreamFactory extends ConstraintStreamFactory {

    ConstraintStreamingSession buildSession();

}
