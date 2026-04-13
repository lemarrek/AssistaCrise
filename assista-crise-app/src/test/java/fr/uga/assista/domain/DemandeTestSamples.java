package fr.uga.assista.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class DemandeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    public static Demande getDemandeSample1() {
        return new Demande().id(1L).titre("titre1").typeBesoin("typeBesoin1");
    }

    public static Demande getDemandeSample2() {
        return new Demande().id(2L).titre("titre2").typeBesoin("typeBesoin2");
    }

    public static Demande getDemandeRandomSampleGenerator() {
        return new Demande().id(longCount.incrementAndGet()).titre(UUID.randomUUID().toString()).typeBesoin(UUID.randomUUID().toString());
    }
}
