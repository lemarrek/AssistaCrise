package fr.uga.assista.web.rest;

import static fr.uga.assista.domain.InformationAsserts.*;
import static fr.uga.assista.web.rest.TestUtil.createUpdateProxyForBean;
import static fr.uga.assista.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.uga.assista.IntegrationTest;
import fr.uga.assista.domain.Crise;
import fr.uga.assista.domain.Information;
import fr.uga.assista.domain.Utilisateur;
import fr.uga.assista.repository.InformationRepository;
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
 * Integration tests for the {@link InformationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class InformationResourceIT {

    private static final String DEFAULT_CONTENU = "AAAAAAAAAA";
    private static final String UPDATED_CONTENU = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_DATE_PUBLICATION = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_PUBLICATION = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String ENTITY_API_URL = "/api/information";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private InformationRepository informationRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInformationMockMvc;

    private Information information;

    private Information insertedInformation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Information createEntity(EntityManager em) {
        Information information = new Information().contenu(DEFAULT_CONTENU).datePublication(DEFAULT_DATE_PUBLICATION);
        // Add required entity
        Utilisateur utilisateur;
        if (TestUtil.findAll(em, Utilisateur.class).isEmpty()) {
            utilisateur = UtilisateurResourceIT.createEntity();
            em.persist(utilisateur);
            em.flush();
        } else {
            utilisateur = TestUtil.findAll(em, Utilisateur.class).get(0);
        }
        information.setAuteur(utilisateur);
        // Add required entity
        Crise crise;
        if (TestUtil.findAll(em, Crise.class).isEmpty()) {
            crise = CriseResourceIT.createEntity();
            em.persist(crise);
            em.flush();
        } else {
            crise = TestUtil.findAll(em, Crise.class).get(0);
        }
        information.setCrise(crise);
        return information;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Information createUpdatedEntity(EntityManager em) {
        Information updatedInformation = new Information().contenu(UPDATED_CONTENU).datePublication(UPDATED_DATE_PUBLICATION);
        // Add required entity
        Utilisateur utilisateur;
        if (TestUtil.findAll(em, Utilisateur.class).isEmpty()) {
            utilisateur = UtilisateurResourceIT.createUpdatedEntity();
            em.persist(utilisateur);
            em.flush();
        } else {
            utilisateur = TestUtil.findAll(em, Utilisateur.class).get(0);
        }
        updatedInformation.setAuteur(utilisateur);
        // Add required entity
        Crise crise;
        if (TestUtil.findAll(em, Crise.class).isEmpty()) {
            crise = CriseResourceIT.createUpdatedEntity();
            em.persist(crise);
            em.flush();
        } else {
            crise = TestUtil.findAll(em, Crise.class).get(0);
        }
        updatedInformation.setCrise(crise);
        return updatedInformation;
    }

    @BeforeEach
    void initTest() {
        information = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedInformation != null) {
            informationRepository.delete(insertedInformation);
            insertedInformation = null;
        }
    }

    @Test
    @Transactional
    void createInformation() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Information
        var returnedInformation = om.readValue(
            restInformationMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(information)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Information.class
        );

        // Validate the Information in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertInformationUpdatableFieldsEquals(returnedInformation, getPersistedInformation(returnedInformation));

        insertedInformation = returnedInformation;
    }

    @Test
    @Transactional
    void createInformationWithExistingId() throws Exception {
        // Create the Information with an existing ID
        information.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInformationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(information)))
            .andExpect(status().isBadRequest());

        // Validate the Information in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDatePublicationIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        information.setDatePublication(null);

        // Create the Information, which fails.

        restInformationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(information)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllInformations() throws Exception {
        // Initialize the database
        insertedInformation = informationRepository.saveAndFlush(information);

        // Get all the informationList
        restInformationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(information.getId().intValue())))
            .andExpect(jsonPath("$.[*].contenu").value(hasItem(DEFAULT_CONTENU)))
            .andExpect(jsonPath("$.[*].datePublication").value(hasItem(sameInstant(DEFAULT_DATE_PUBLICATION))));
    }

    @Test
    @Transactional
    void getInformation() throws Exception {
        // Initialize the database
        insertedInformation = informationRepository.saveAndFlush(information);

        // Get the information
        restInformationMockMvc
            .perform(get(ENTITY_API_URL_ID, information.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(information.getId().intValue()))
            .andExpect(jsonPath("$.contenu").value(DEFAULT_CONTENU))
            .andExpect(jsonPath("$.datePublication").value(sameInstant(DEFAULT_DATE_PUBLICATION)));
    }

    @Test
    @Transactional
    void getNonExistingInformation() throws Exception {
        // Get the information
        restInformationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingInformation() throws Exception {
        // Initialize the database
        insertedInformation = informationRepository.saveAndFlush(information);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the information
        Information updatedInformation = informationRepository.findById(information.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedInformation are not directly saved in db
        em.detach(updatedInformation);
        updatedInformation.contenu(UPDATED_CONTENU).datePublication(UPDATED_DATE_PUBLICATION);

        restInformationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedInformation.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedInformation))
            )
            .andExpect(status().isOk());

        // Validate the Information in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedInformationToMatchAllProperties(updatedInformation);
    }

    @Test
    @Transactional
    void putNonExistingInformation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        information.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInformationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, information.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(information))
            )
            .andExpect(status().isBadRequest());

        // Validate the Information in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInformation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        information.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInformationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(information))
            )
            .andExpect(status().isBadRequest());

        // Validate the Information in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInformation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        information.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInformationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(information)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Information in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInformationWithPatch() throws Exception {
        // Initialize the database
        insertedInformation = informationRepository.saveAndFlush(information);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the information using partial update
        Information partialUpdatedInformation = new Information();
        partialUpdatedInformation.setId(information.getId());

        restInformationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInformation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInformation))
            )
            .andExpect(status().isOk());

        // Validate the Information in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInformationUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedInformation, information),
            getPersistedInformation(information)
        );
    }

    @Test
    @Transactional
    void fullUpdateInformationWithPatch() throws Exception {
        // Initialize the database
        insertedInformation = informationRepository.saveAndFlush(information);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the information using partial update
        Information partialUpdatedInformation = new Information();
        partialUpdatedInformation.setId(information.getId());

        partialUpdatedInformation.contenu(UPDATED_CONTENU).datePublication(UPDATED_DATE_PUBLICATION);

        restInformationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInformation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInformation))
            )
            .andExpect(status().isOk());

        // Validate the Information in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInformationUpdatableFieldsEquals(partialUpdatedInformation, getPersistedInformation(partialUpdatedInformation));
    }

    @Test
    @Transactional
    void patchNonExistingInformation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        information.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInformationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, information.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(information))
            )
            .andExpect(status().isBadRequest());

        // Validate the Information in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInformation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        information.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInformationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(information))
            )
            .andExpect(status().isBadRequest());

        // Validate the Information in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInformation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        information.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInformationMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(information)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Information in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInformation() throws Exception {
        // Initialize the database
        insertedInformation = informationRepository.saveAndFlush(information);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the information
        restInformationMockMvc
            .perform(delete(ENTITY_API_URL_ID, information.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return informationRepository.count();
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

    protected Information getPersistedInformation(Information information) {
        return informationRepository.findById(information.getId()).orElseThrow();
    }

    protected void assertPersistedInformationToMatchAllProperties(Information expectedInformation) {
        assertInformationAllPropertiesEquals(expectedInformation, getPersistedInformation(expectedInformation));
    }

    protected void assertPersistedInformationToMatchUpdatableProperties(Information expectedInformation) {
        assertInformationAllUpdatablePropertiesEquals(expectedInformation, getPersistedInformation(expectedInformation));
    }
}
