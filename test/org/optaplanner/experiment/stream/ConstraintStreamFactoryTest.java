package org.optaplanner.experiment.stream;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;
import org.optaplanner.experiment.example.Person;
import org.optaplanner.experiment.example.Shift;
import org.optaplanner.experiment.example.Unavailability;
import org.optaplanner.experiment.stream.api.ConstraintStreamFactory;
import org.optaplanner.experiment.stream.api.bi.BiJoiner;
import org.optaplanner.experiment.stream.impl.ConstraintStreamingSession;
import org.optaplanner.experiment.stream.impl.InnerConstraintStreamFactory;
import org.optaplanner.experiment.stream.impl.bavet.BavetConstraintStreamFactory;

import static java.util.stream.Collectors.*;
import static org.junit.Assert.assertEquals;

public class ConstraintStreamFactoryTest {

    @Test
    public void javaFilterStream() {
        List<Shift> list = Stream.of(
                new Shift(1, null),
                new Shift(1, "Ann"),
                new Shift(2, "Beth"),
                new Shift(2, "Beth"),
                new Shift(3, "Carl"))
                .filter(shift -> shift.getPersonName() != null
                        && shift.getPersonName().equals("Beth"))
                .collect(toList());
        long score = 0L;
        for (Shift shift : list) {
            score--;
        }
        assertEquals(-2L, score);
    }

    @Test
    public void filterStream() {
        ConstraintStreamFactory factory = new BavetConstraintStreamFactory();

        factory.select(Shift.class)
                .filter(shift -> shift.getPersonName() != null
                        && shift.getPersonName().equals("Beth"))
                .scoreEachMatch("Don't assign Beth");
//        factory.select(Shift.class)
//                .filter(shift -> shift.getPersonName() != null
//                        && !shift.getPersonName().equals("Carl"))
//                .scoreEachMatch("Do assign Carl");

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
                        BiJoiner.equals(Shift::getPersonName, Unavailability::getPersonName))
                .filter((shift, unavailability) -> shift.getTime() >= unavailability.getTimeStart()
                        && shift.getTime() < unavailability.getTimeEnd())
                .scoreEachMatch("Unavailable person");

//        factory.select(Shift.class)
//                .filter(shift -> shift.getPersonName() != null)
//                .join(factory.select(Unavailability.class),
//                        equals(Shift::getPersonName, Unavailability::getPersonName),
//                        between(Shift::getTime, Unavailability::getTimeStart, Unavailability::getTimeEnd))
//                .scoreEachMatch("Unavailable person");

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

    @Test
    public void javaGroupByStream() {
        Map<String, Long> personLoad = Stream.of(
                new Shift(1, null),
                new Shift(1, "Ann"),
                new Shift(2, "Beth"),
                new Shift(2, "Beth"),
                new Shift(1, "Carl"),
                new Shift(2, "Carl"),
                new Shift(3, "Carl"))
                .filter(s -> s.getPersonName() != null)
                .collect(
                        Collectors.groupingBy(
                                Shift::getPersonName, Collectors.counting()
                        )
                );
        long score = 0L;
        for (Map.Entry<String, Long> entry : personLoad.entrySet()) {
            if (entry.getValue() > 2L) {
                score--;
            }
        }
        assertEquals(-1L, score);
    }

    @Test
    public void accumulateStream() {
        ConstraintStreamFactory factory = new BavetConstraintStreamFactory();

//        factory.select(Person.class)
//                .join(factory.select(Shift.class),
//                        BiJoiner.equals(Person::getName, Shift::getPersonName))
//                // TODO
//                .scoreEachMatch("Maximum 2 shifts per person");

//        factory.select(Shift.class)
//                .groupBy(Shift::getPersonName, summingInt(Shift::getTime))
//                .filter((personName, shiftCount) -> (shiftCount > 2))
//                .scoreEachMatch("Maximum 2 shifts per person");

        ConstraintStreamingSession session = ((InnerConstraintStreamFactory) factory).buildSession();

        session.insert(new Person("Ann"));
        session.insert(new Person("Beth"));
        session.insert(new Person("Carl"));
        session.insert(new Person("Dora"));
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

        assertEquals(-1L, session.calculateScore());
        session.retract(f);
        assertEquals(0L, session.calculateScore());
        session.insert(f);
        assertEquals(-1L, session.calculateScore());
        session.insert(new Shift(3, "Beth"));
        assertEquals(-2L, session.calculateScore());
    }

    @Test
    public void javaCountStream() {
        long carlCount = Stream.of(
                new Shift(1, null),
                new Shift(1, "Ann"),
                new Shift(2, "Beth"),
                new Shift(2, "Beth"),
                new Shift(1, "Carl"),
                new Shift(2, "Carl"),
                new Shift(3, "Carl"))
                .filter(s -> Objects.equals(s.getPersonName(), "Carl"))
                .count();
        long score = 0L;
        if (carlCount > 2L) {
            score--;
        }
        assertEquals(-1L, score);
    }

    @Test
    public void javaReduceStream() {
        long carlCount = Stream.of(
                new Shift(1, null),
                new Shift(1, "Ann"),
                new Shift(2, "Beth"),
                new Shift(2, "Beth"),
                new Shift(1, "Carl"),
                new Shift(2, "Carl"),
                new Shift(3, "Carl"))
                .filter(s -> Objects.equals(s.getPersonName(), "Carl"))
                .map(s -> 1L)
                .reduce(0L, (a, b) -> a + b);
        long score = 0L;
        if (carlCount > 2L) {
            score--;
        }
        assertEquals(-1L, score);
    }

}
