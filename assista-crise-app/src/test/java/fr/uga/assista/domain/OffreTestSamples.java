package fr.uga.assista.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class OffreTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    public static Offre getOffreSample1() {
        return new Offre().id(1L).titre("titre1").servicePropose("servicePropose1");
    }

    public static Offre getOffreSample2() {
        return new Offre().id(2L).titre("titre2").servicePropose("servicePropose2");
    }

    public static Offre getOffreRandomSampleGenerator() {
        return new Offre().id(longCount.incrementAndGet()).titre(UUID.randomUUID().toString()).servicePropose(UUID.randomUUID().toString());
    }
}
