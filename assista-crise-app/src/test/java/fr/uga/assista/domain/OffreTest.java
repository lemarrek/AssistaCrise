package fr.uga.assista.domain;

import static fr.uga.assista.domain.CriseTestSamples.*;
import static fr.uga.assista.domain.OffreTestSamples.*;
import static fr.uga.assista.domain.UtilisateurTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.uga.assista.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OffreTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Offre.class);
        Offre offre1 = getOffreSample1();
        Offre offre2 = new Offre();
        assertThat(offre1).isNotEqualTo(offre2);

        offre2.setId(offre1.getId());
        assertThat(offre1).isEqualTo(offre2);

        offre2 = getOffreSample2();
        assertThat(offre1).isNotEqualTo(offre2);
    }

    @Test
    void auteurTest() {
        Offre offre = getOffreRandomSampleGenerator();
        Utilisateur utilisateurBack = getUtilisateurRandomSampleGenerator();

        offre.setAuteur(utilisateurBack);
        assertThat(offre.getAuteur()).isEqualTo(utilisateurBack);

        offre.auteur(null);
        assertThat(offre.getAuteur()).isNull();
    }

    @Test
    void criseTest() {
        Offre offre = getOffreRandomSampleGenerator();
        Crise criseBack = getCriseRandomSampleGenerator();

        offre.setCrise(criseBack);
        assertThat(offre.getCrise()).isEqualTo(criseBack);

        offre.crise(null);
        assertThat(offre.getCrise()).isNull();
    }
}
