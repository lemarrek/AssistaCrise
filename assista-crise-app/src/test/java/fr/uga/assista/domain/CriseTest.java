package fr.uga.assista.domain;

import static fr.uga.assista.domain.CriseTestSamples.*;
import static fr.uga.assista.domain.UtilisateurTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.uga.assista.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CriseTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Crise.class);
        Crise crise1 = getCriseSample1();
        Crise crise2 = new Crise();
        assertThat(crise1).isNotEqualTo(crise2);

        crise2.setId(crise1.getId());
        assertThat(crise1).isEqualTo(crise2);

        crise2 = getCriseSample2();
        assertThat(crise1).isNotEqualTo(crise2);
    }

    @Test
    void validateurTest() {
        Crise crise = getCriseRandomSampleGenerator();
        Utilisateur utilisateurBack = getUtilisateurRandomSampleGenerator();

        crise.setValidateur(utilisateurBack);
        assertThat(crise.getValidateur()).isEqualTo(utilisateurBack);

        crise.validateur(null);
        assertThat(crise.getValidateur()).isNull();
    }

    @Test
    void sinistresTest() {
        Crise crise = getCriseRandomSampleGenerator();
        Utilisateur utilisateurBack = getUtilisateurRandomSampleGenerator();

        crise.addSinistres(utilisateurBack);
        assertThat(crise.getSinistreses()).containsOnly(utilisateurBack);
        assertThat(utilisateurBack.getCrisesToucheeses()).containsOnly(crise);

        crise.removeSinistres(utilisateurBack);
        assertThat(crise.getSinistreses()).doesNotContain(utilisateurBack);
        assertThat(utilisateurBack.getCrisesToucheeses()).doesNotContain(crise);

        crise.sinistreses(new HashSet<>(Set.of(utilisateurBack)));
        assertThat(crise.getSinistreses()).containsOnly(utilisateurBack);
        assertThat(utilisateurBack.getCrisesToucheeses()).containsOnly(crise);

        crise.setSinistreses(new HashSet<>());
        assertThat(crise.getSinistreses()).doesNotContain(utilisateurBack);
        assertThat(utilisateurBack.getCrisesToucheeses()).doesNotContain(crise);
    }
}
