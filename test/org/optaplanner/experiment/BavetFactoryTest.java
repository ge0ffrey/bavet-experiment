package org.optaplanner.experiment;

import org.junit.Test;
import org.optaplanner.experiment.bavet.BavetFactory;
import org.optaplanner.experiment.example.Person;
import org.optaplanner.experiment.example.Shift;
import org.optaplanner.experiment.example.Unavailability;

import static org.junit.Assert.assertEquals;

public class BavetFactoryTest {

    @Test
    public void insertSOnly() {
        BavetFactory bavetFactory = new BavetFactory();

        bavetFactory.select(Person.class)
                .filter(person -> !person.getName().equals("Carl"))
                .forEachMatch("Ladies first");

        bavetFactory.select(Shift.class)
                .filter(shift -> shift.getPersonName() != null)
                .join(bavetFactory.select(Unavailability.class),
                        (shift, unavailability) -> shift.getPersonName().equals(unavailability.getName())
                        && shift.getTime() >= unavailability.getTimeStart()
                        && shift.getTime() < unavailability.getTimeEnd())
                .forEachMatch("Check unavailability");

        Person ann = new Person("Ann");
        bavetFactory.insert(ann);
        bavetFactory.insert(new Person("Beth"));
        bavetFactory.insert(new Person("Carl"));
        bavetFactory.insert(new Person("Dora"));
        bavetFactory.insert(new Shift(1, null));
        bavetFactory.insert(new Shift(1, "Ann"));
        bavetFactory.insert(new Shift(2, "Beth"));
        bavetFactory.insert(new Shift(2, "Beth"));
        bavetFactory.insert(new Shift(3, "Carl"));
        bavetFactory.insert(new Unavailability("Carl", 2, 4));
        bavetFactory.insert(new Unavailability("Dora", 2, 4));

        assertEquals(-3L, bavetFactory.calculateScore());
        bavetFactory.retract(ann);
        assertEquals(-2L, bavetFactory.calculateScore());

    }

}
