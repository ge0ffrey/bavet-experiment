package org.optaplanner.experiment.stream;

import org.junit.Test;
import org.optaplanner.experiment.example.Person;
import org.optaplanner.experiment.example.Shift;
import org.optaplanner.experiment.example.Unavailability;
import org.optaplanner.experiment.stream.impl.ConstraintStreamingSession;
import org.optaplanner.experiment.stream.impl.InnerConstraintStreamFactory;
import org.optaplanner.experiment.stream.impl.bavet.BavetConstraintStreamFactory;

import static org.junit.Assert.assertEquals;

public class ConstraintStreamFactoryTest {

    @Test
    public void insertSOnly() {
        ConstraintStreamFactory factory = new BavetConstraintStreamFactory();

        factory.select(Person.class)
                .filter(person -> !person.getName().equals("Carl"))
                .scoreEachMatch("Ladies first");

//        factory.select(Shift.class)
//                .filter(shift -> shift.getPersonName() != null)
//                .join(factory.select(Unavailability.class),
//                        (shift, unavailability) -> shift.getPersonName().equals(unavailability.getName())
//                        && shift.getTime() >= unavailability.getTimeStart()
//                        && shift.getTime() < unavailability.getTimeEnd())
//                .forEachMatch("Check unavailability");

        ConstraintStreamingSession session = ((InnerConstraintStreamFactory) factory).buildSession();

        Person ann = new Person("Ann");
        session.insert(ann);
        session.insert(new Person("Beth"));
        session.insert(new Person("Carl"));
        session.insert(new Person("Dora"));
        session.insert(new Shift(1, null));
        session.insert(new Shift(1, "Ann"));
        session.insert(new Shift(2, "Beth"));
        session.insert(new Shift(2, "Beth"));
        session.insert(new Shift(3, "Carl"));
        session.insert(new Unavailability("Carl", 2, 4));
        session.insert(new Unavailability("Dora", 2, 4));

        assertEquals(-3L, session.calculateScore());
        session.retract(ann);
        assertEquals(-2L, session.calculateScore());

    }

}
