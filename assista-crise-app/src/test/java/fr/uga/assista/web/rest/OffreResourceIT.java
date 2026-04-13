package fr.uga.assista.web.rest;

import static fr.uga.assista.domain.OffreAsserts.*;
import static fr.uga.assista.web.rest.TestUtil.createUpdateProxyForBean;
import static fr.uga.assista.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.uga.assista.IntegrationTest;
import fr.uga.assista.domain.Crise;
import fr.uga.assista.domain.Offre;
import fr.uga.assista.domain.Utilisateur;
import fr.uga.assista.domain.enumeration.StatutAnnonce;
import fr.uga.assista.repository.OffreRepository;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link OffreResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OffreResourceIT {

    private static final String DEFAULT_TITRE = "AAAAAAAAAA";
    private static final String UPDATED_TITRE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_SERVICE_PROPOSE = "AAAAAAAAAA";
    private static final String UPDATED_SERVICE_PROPOSE = "BBBBBBBBBB";

    private static final Double DEFAULT_LATITUDE = 1D;
    private static final Double UPDATED_LATITUDE = 2D;

    private static final Double DEFAULT_LONGITUDE = 1D;
    private static final Double UPDATED_LONGITUDE = 2D;

    private static final StatutAnnonce DEFAULT_STATUT = StatutAnnonce.OUVERTE;
    private static final StatutAnnonce UPDATED_STATUT = StatutAnnonce.EN_COURS_DE_RESOLUTION;

    private static final ZonedDateTime DEFAULT_DATE_CREATION = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_CREATION = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String ENTITY_API_URL = "/api/offres";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private OffreRepository offreRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOffreMockMvc;

    private Offre offre;

    private Offre insertedOffre;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Offre createEntity(EntityManager em) {
        Offre offre = new Offre()
            .titre(DEFAULT_TITRE)
            .description(DEFAULT_DESCRIPTION)
            .servicePropose(DEFAULT_SERVICE_PROPOSE)
            .latitude(DEFAULT_LATITUDE)
            .longitude(DEFAULT_LONGITUDE)
            .statut(DEFAULT_STATUT)
            .dateCreation(DEFAULT_DATE_CREATION);
        // Add required entity
        Utilisateur utilisateur;
        if (TestUtil.findAll(em, Utilisateur.class).isEmpty()) {
            utilisateur = UtilisateurResourceIT.createEntity();
            em.persist(utilisateur);
            em.flush();
        } else {
            utilisateur = TestUtil.findAll(em, Utilisateur.class).get(0);
        }
        offre.setAuteur(utilisateur);
        // Add required entity
        Crise crise;
        if (TestUtil.findAll(em, Crise.class).isEmpty()) {
            crise = CriseResourceIT.createEntity();
            em.persist(crise);
            em.flush();
        } else {
            crise = TestUtil.findAll(em, Crise.class).get(0);
        }
        offre.setCrise(crise);
        return offre;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Offre createUpdatedEntity(EntityManager em) {
        Offre updatedOffre = new Offre()
            .titre(UPDATED_TITRE)
            .description(UPDATED_DESCRIPTION)
            .servicePropose(UPDATED_SERVICE_PROPOSE)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE)
            .statut(UPDATED_STATUT)
            .dateCreation(UPDATED_DATE_CREATION);
        // Add required entity
        Utilisateur utilisateur;
        if (TestUtil.findAll(em, Utilisateur.class).isEmpty()) {
            utilisateur = UtilisateurResourceIT.createUpdatedEntity();
            em.persist(utilisateur);
            em.flush();
        } else {
            utilisateur = TestUtil.findAll(em, Utilisateur.class).get(0);
        }
        updatedOffre.setAuteur(utilisateur);
        // Add required entity
        Crise crise;
        if (TestUtil.findAll(em, Crise.class).isEmpty()) {
            crise = CriseResourceIT.createUpdatedEntity();
            em.persist(crise);
            em.flush();
        } else {
            crise = TestUtil.findAll(em, Crise.class).get(0);
        }
        updatedOffre.setCrise(crise);
        return updatedOffre;
    }

    @BeforeEach
    void initTest() {
        offre = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedOffre != null) {
            offreRepository.delete(insertedOffre);
            insertedOffre = null;
        }
    }

    @Test
    @Transactional
    void createOffre() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Offre
        var returnedOffre = om.readValue(
            restOffreMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(offre)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Offre.class
        );

        // Validate the Offre in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertOffreUpdatableFieldsEquals(returnedOffre, getPersistedOffre(returnedOffre));

        insertedOffre = returnedOffre;
    }

    @Test
    @Transactional
    void createOffreWithExistingId() throws Exception {
        // Create the Offre with an existing ID
        offre.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOffreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(offre)))
            .andExpect(status().isBadRequest());

        // Validate the Offre in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitreIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        offre.setTitre(null);

        // Create the Offre, which fails.

        restOffreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(offre)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkServiceProposeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        offre.setServicePropose(null);

        // Create the Offre, which fails.

        restOffreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(offre)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLatitudeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        offre.setLatitude(null);

        // Create the Offre, which fails.

        restOffreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(offre)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLongitudeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        offre.setLongitude(null);

        // Create the Offre, which fails.

        restOffreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(offre)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatutIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        offre.setStatut(null);

        // Create the Offre, which fails.

        restOffreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(offre)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateCreationIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        offre.setDateCreation(null);

        // Create the Offre, which fails.

        restOffreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(offre)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllOffres() throws Exception {
        // Initialize the database
        insertedOffre = offreRepository.saveAndFlush(offre);

        // Get all the offreList
        restOffreMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(offre.getId().intValue())))
            .andExpect(jsonPath("$.[*].titre").value(hasItem(DEFAULT_TITRE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].servicePropose").value(hasItem(DEFAULT_SERVICE_PROPOSE)))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE)))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE)))
            .andExpect(jsonPath("$.[*].statut").value(hasItem(DEFAULT_STATUT.toString())))
            .andExpect(jsonPath("$.[*].dateCreation").value(hasItem(sameInstant(DEFAULT_DATE_CREATION))));
    }

    @Test
    @Transactional
    void getOffre() throws Exception {
        // Initialize the database
        insertedOffre = offreRepository.saveAndFlush(offre);

        // Get the offre
        restOffreMockMvc
            .perform(get(ENTITY_API_URL_ID, offre.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(offre.getId().intValue()))
            .andExpect(jsonPath("$.titre").value(DEFAULT_TITRE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.servicePropose").value(DEFAULT_SERVICE_PROPOSE))
            .andExpect(jsonPath("$.latitude").value(DEFAULT_LATITUDE))
            .andExpect(jsonPath("$.longitude").value(DEFAULT_LONGITUDE))
            .andExpect(jsonPath("$.statut").value(DEFAULT_STATUT.toString()))
            .andExpect(jsonPath("$.dateCreation").value(sameInstant(DEFAULT_DATE_CREATION)));
    }

    @Test
    @Transactional
    void getNonExistingOffre() throws Exception {
        // Get the offre
        restOffreMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOffre() throws Exception {
        // Initialize the database
        insertedOffre = offreRepository.saveAndFlush(offre);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the offre
        Offre updatedOffre = offreRepository.findById(offre.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedOffre are not directly saved in db
        em.detach(updatedOffre);
        updatedOffre
            .titre(UPDATED_TITRE)
            .description(UPDATED_DESCRIPTION)
            .servicePropose(UPDATED_SERVICE_PROPOSE)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE)
            .statut(UPDATED_STATUT)
            .dateCreation(UPDATED_DATE_CREATION);

        restOffreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOffre.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedOffre))
            )
            .andExpect(status().isOk());

        // Validate the Offre in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedOffreToMatchAllProperties(updatedOffre);
    }

    @Test
    @Transactional
    void putNonExistingOffre() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        offre.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOffreMockMvc
            .perform(put(ENTITY_API_URL_ID, offre.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(offre)))
            .andExpect(status().isBadRequest());

        // Validate the Offre in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOffre() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        offre.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOffreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(offre))
            )
            .andExpect(status().isBadRequest());

        // Validate the Offre in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOffre() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        offre.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOffreMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(offre)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Offre in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOffreWithPatch() throws Exception {
        // Initialize the database
        insertedOffre = offreRepository.saveAndFlush(offre);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the offre using partial update
        Offre partialUpdatedOffre = new Offre();
        partialUpdatedOffre.setId(offre.getId());

        partialUpdatedOffre
            .titre(UPDATED_TITRE)
            .description(UPDATED_DESCRIPTION)
            .servicePropose(UPDATED_SERVICE_PROPOSE)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE)
            .statut(UPDATED_STATUT)
            .dateCreation(UPDATED_DATE_CREATION);

        restOffreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOffre.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOffre))
            )
            .andExpect(status().isOk());

        // Validate the Offre in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOffreUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedOffre, offre), getPersistedOffre(offre));
    }

    @Test
    @Transactional
    void fullUpdateOffreWithPatch() throws Exception {
        // Initialize the database
        insertedOffre = offreRepository.saveAndFlush(offre);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the offre using partial update
        Offre partialUpdatedOffre = new Offre();
        partialUpdatedOffre.setId(offre.getId());

        partialUpdatedOffre
            .titre(UPDATED_TITRE)
            .description(UPDATED_DESCRIPTION)
            .servicePropose(UPDATED_SERVICE_PROPOSE)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE)
            .statut(UPDATED_STATUT)
            .dateCreation(UPDATED_DATE_CREATION);

        restOffreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOffre.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOffre))
            )
            .andExpect(status().isOk());

        // Validate the Offre in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOffreUpdatableFieldsEquals(partialUpdatedOffre, getPersistedOffre(partialUpdatedOffre));
    }

    @Test
    @Transactional
    void patchNonExistingOffre() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        offre.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOffreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, offre.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(offre))
            )
            .andExpect(status().isBadRequest());

        // Validate the Offre in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOffre() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        offre.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOffreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(offre))
            )
            .andExpect(status().isBadRequest());

        // Validate the Offre in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOffre() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        offre.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOffreMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(offre)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Offre in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOffre() throws Exception {
        // Initialize the database
        insertedOffre = offreRepository.saveAndFlush(offre);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the offre
        restOffreMockMvc
            .perform(delete(ENTITY_API_URL_ID, offre.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return offreRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Offre getPersistedOffre(Offre offre) {
        return offreRepository.findById(offre.getId()).orElseThrow();
    }

    protected void assertPersistedOffreToMatchAllProperties(Offre expectedOffre) {
        assertOffreAllPropertiesEquals(expectedOffre, getPersistedOffre(expectedOffre));
    }

    protected void assertPersistedOffreToMatchUpdatableProperties(Offre expectedOffre) {
        assertOffreAllUpdatablePropertiesEquals(expectedOffre, getPersistedOffre(expectedOffre));
    }
}
