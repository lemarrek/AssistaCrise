package fr.uga.assista.domain;

import static fr.uga.assista.domain.CriseTestSamples.*;
import static fr.uga.assista.domain.UtilisateurTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.uga.assista.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class UtilisateurTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Utilisateur.class);
        Utilisateur utilisateur1 = getUtilisateurSample1();
        Utilisateur utilisateur2 = new Utilisateur();
        assertThat(utilisateur1).isNotEqualTo(utilisateur2);

        utilisateur2.setId(utilisateur1.getId());
        assertThat(utilisateur1).isEqualTo(utilisateur2);

        utilisateur2 = getUtilisateurSample2();
        assertThat(utilisateur1).isNotEqualTo(utilisateur2);
    }

    @Test
    void crisesToucheesTest() {
        Utilisateur utilisateur = getUtilisateurRandomSampleGenerator();
        Crise criseBack = getCriseRandomSampleGenerator();

        utilisateur.addCrisesTouchees(criseBack);
        assertThat(utilisateur.getCrisesToucheeses()).containsOnly(criseBack);

        utilisateur.removeCrisesTouchees(criseBack);
        assertThat(utilisateur.getCrisesToucheeses()).doesNotContain(criseBack);

        utilisateur.crisesToucheeses(new HashSet<>(Set.of(criseBack)));
        assertThat(utilisateur.getCrisesToucheeses()).containsOnly(criseBack);

        utilisateur.setCrisesToucheeses(new HashSet<>());
        assertThat(utilisateur.getCrisesToucheeses()).doesNotContain(criseBack);
    }
}
