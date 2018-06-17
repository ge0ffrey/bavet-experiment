package org.optaplanner.experiment.stream;

import org.junit.Test;
import org.optaplanner.experiment.example.Person;
import org.optaplanner.experiment.example.Shift;
import org.optaplanner.experiment.example.Unavailability;
import org.optaplanner.experiment.stream.api.ConstraintStreamFactory;
import org.optaplanner.experiment.stream.api.bi.BiJoiner;
import org.optaplanner.experiment.stream.impl.ConstraintStreamingSession;
import org.optaplanner.experiment.stream.impl.InnerConstraintStreamFactory;
import org.optaplanner.experiment.stream.impl.bavet.BavetConstraintStreamFactory;

import static org.junit.Assert.assertEquals;

public class ConstraintStreamFactoryTest {

    @Test
    public void uniStream() {
        ConstraintStreamFactory factory = new BavetConstraintStreamFactory();

        factory.select(Shift.class)
                .filter(shift -> shift.getPersonName() != null && shift.getPersonName().equals("Beth"))
                .scoreEachMatch("Don't assign Beth");

        ConstraintStreamingSession session = ((InnerConstraintStreamFactory) factory).buildSession();

        session.insert(new Person("Ann"));
        session.insert(new Person("Beth"));
        session.insert(new Person("Carl"));
        session.insert(new Person("Dora"));
        session.insert(new Unavailability("Carl", 2, 4));
        session.insert(new Unavailability("Dora", 2, 4));
        Shift a = new Shift(1, null);
        session.insert(a);
        Shift b = new Shift(1, "Ann");
        session.insert(b);
        Shift c = new Shift(2, "Beth");
        session.insert(c);
        Shift d = new Shift(2, "Beth");
        session.insert(d);
        Shift e = new Shift(3, "Carl");
        session.insert(e);

        assertEquals(-2L, session.calculateScore());
        session.retract(c);
        assertEquals(-1L, session.calculateScore());
        session.insert(c);
        assertEquals(-2L, session.calculateScore());
    }

    @Test
    public void joinStream() {
        ConstraintStreamFactory factory = new BavetConstraintStreamFactory();

        factory.select(Shift.class)
                .filter(shift -> shift.getPersonName() != null)
                .join(factory.select(Unavailability.class),
                        BiJoiner.equals(Shift::getPersonName, Unavailability::getName))
                .filter((shift, unavailability) -> shift.getTime() >= unavailability.getTimeStart()
                        && shift.getTime() < unavailability.getTimeEnd())
                .scoreEachMatch("Unavailable person");

        ConstraintStreamingSession session = ((InnerConstraintStreamFactory) factory).buildSession();

        session.insert(new Person("Ann"));
        session.insert(new Person("Beth"));
        session.insert(new Person("Carl"));
        session.insert(new Person("Dora"));
        Unavailability carlUnavailability = new Unavailability("Carl", 2, 4);
        session.insert(carlUnavailability);
        session.insert(new Unavailability("Dora", 2, 4));
        Shift a = new Shift(1, null);
        session.insert(a);
        Shift b = new Shift(1, "Ann");
        session.insert(b);
        Shift c = new Shift(2, "Beth");
        session.insert(c);
        Shift d = new Shift(2, "Beth");
        session.insert(d);
        Shift e = new Shift(1, "Carl");
        session.insert(e);
        Shift f = new Shift(2, "Carl");
        session.insert(f);
        Shift g = new Shift(3, "Carl");
        session.insert(g);

        assertEquals(-2L, session.calculateScore());
        session.retract(f);
        assertEquals(-1L, session.calculateScore());
        session.insert(f);
        assertEquals(-2L, session.calculateScore());
        session.retract(carlUnavailability);
        assertEquals(0L, session.calculateScore());
    }

}
