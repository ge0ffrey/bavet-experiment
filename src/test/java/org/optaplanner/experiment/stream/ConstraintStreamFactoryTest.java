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
import org.optaplanner.experiment.stream.api.collector.ConstraintStreamCollectors;
import org.optaplanner.experiment.stream.impl.ConstraintStreamingSession;
import org.optaplanner.experiment.stream.impl.InnerConstraintStreamFactory;
import org.optaplanner.experiment.stream.impl.bavet.BavetConstraintStreamFactory;

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
                .collect(Collectors.toList());
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
    public void countingStream() {
        ConstraintStreamFactory factory = new BavetConstraintStreamFactory();

        factory.select(Shift.class)
                .groupBy(Shift::getPersonName, ConstraintStreamCollectors.counting())
                .filter((personName, shiftCount) -> (shiftCount > 2))
                .scoreEachMatch("Maximum 2 shifts per person");

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
    public void summingStream() {
        ConstraintStreamFactory factory = new BavetConstraintStreamFactory();

        factory.select(Shift.class)
                .groupBy(Shift::getPersonName, ConstraintStreamCollectors.summingLong(Shift::getDifficulty))
                .filter((personName, difficultySum) -> (difficultySum > 10_000L))
                .scoreEachMatch("Maximum 10k difficulty per person");

        ConstraintStreamingSession session = ((InnerConstraintStreamFactory) factory).buildSession();

        session.insert(new Person("Ann"));
        session.insert(new Person("Beth"));
        session.insert(new Person("Carl"));
        session.insert(new Person("Dora"));
        Shift a = new Shift(1, 100L, null);
        session.insert(a);
        Shift b = new Shift(1, 100L, "Ann");
        session.insert(b);
        Shift c = new Shift(2, 100L, "Beth");
        session.insert(c);
        Shift d = new Shift(2, 2_000L, "Beth");
        session.insert(d);
        Shift e = new Shift(1, 100L, "Carl");
        session.insert(e);
        Shift f = new Shift(2, 8_000L, "Carl");
        session.insert(f);
        Shift g = new Shift(3, 2_000L, "Carl");
        session.insert(g);

        assertEquals(-1L, session.calculateScore());
        session.retract(f);
        assertEquals(0L, session.calculateScore());
        session.insert(f);
        assertEquals(-1L, session.calculateScore());
        session.insert(new Shift(3, 8_000L, "Beth"));
        assertEquals(-2L, session.calculateScore());

        // TODO test if 3 shifts of Beth are in the system, the total is their sum
        // TODO test if 2 shifts of Beth with difficulty 0, the total is 0
        // TODO test if 0 shifts of Beth end up there, there is no total (no insert)
    }

    @Test
    public void summingStream2() {
        ConstraintStreamFactory factory = new BavetConstraintStreamFactory();

        factory.select(Shift.class)
                .groupBy(Shift::getPersonName, ConstraintStreamCollectors.summingLong(Shift::getDifficulty))
                .filter((personName, difficultySum) -> (difficultySum > 10_000L))
                .scoreEachMatch("Maximum 10k difficulty per person", (personName, difficultySum) -> (difficultySum - 10_000L));

        ConstraintStreamingSession session = ((InnerConstraintStreamFactory) factory).buildSession();

        session.insert(new Person("Ann"));
        session.insert(new Person("Beth"));
        session.insert(new Person("Carl"));
        session.insert(new Person("Dora"));
        Shift a = new Shift(1, 100L, null);
        session.insert(a);
        Shift b = new Shift(1, 100L, "Ann");
        session.insert(b);
        Shift c = new Shift(2, 100L, "Beth");
        session.insert(c);
        Shift d = new Shift(2, 2_000L, "Beth");
        session.insert(d);
        Shift e = new Shift(1, 100L, "Carl");
        session.insert(e);
        Shift f = new Shift(2, 8_000L, "Carl");
        session.insert(f);
        Shift g = new Shift(3, 2_000L, "Carl");
        session.insert(g);

        assertEquals(-100L, session.calculateScore());
        session.retract(f);
        assertEquals(0L, session.calculateScore());
        session.insert(f);
        assertEquals(-100L, session.calculateScore());
        session.insert(new Shift(3, 9_000L, "Beth"));
        assertEquals(-1200L, session.calculateScore());

        // TODO test if 3 shifts of Beth are in the system, the total is their sum
        // TODO test if 2 shifts of Beth with difficulty 0, the total is 0
        // TODO test if 0 shifts of Beth end up there, there is no total (no insert)
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
