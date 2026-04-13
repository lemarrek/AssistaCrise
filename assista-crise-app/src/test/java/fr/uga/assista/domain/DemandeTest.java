package fr.uga.assista.domain;

import static fr.uga.assista.domain.CriseTestSamples.*;
import static fr.uga.assista.domain.DemandeTestSamples.*;
import static fr.uga.assista.domain.UtilisateurTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.uga.assista.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DemandeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Demande.class);
        Demande demande1 = getDemandeSample1();
        Demande demande2 = new Demande();
        assertThat(demande1).isNotEqualTo(demande2);

        demande2.setId(demande1.getId());
        assertThat(demande1).isEqualTo(demande2);

        demande2 = getDemandeSample2();
        assertThat(demande1).isNotEqualTo(demande2);
    }

    @Test
    void auteurTest() {
        Demande demande = getDemandeRandomSampleGenerator();
        Utilisateur utilisateurBack = getUtilisateurRandomSampleGenerator();

        demande.setAuteur(utilisateurBack);
        assertThat(demande.getAuteur()).isEqualTo(utilisateurBack);

        demande.auteur(null);
        assertThat(demande.getAuteur()).isNull();
    }

    @Test
    void criseTest() {
        Demande demande = getDemandeRandomSampleGenerator();
        Crise criseBack = getCriseRandomSampleGenerator();

        demande.setCrise(criseBack);
        assertThat(demande.getCrise()).isEqualTo(criseBack);

        demande.crise(null);
        assertThat(demande.getCrise()).isNull();
    }
}
