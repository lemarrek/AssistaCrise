package fr.uga.assista.service;

import fr.uga.assista.domain.Utilisateur;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link fr.uga.assista.domain.Utilisateur}.
 */
public interface UtilisateurService {
    /**
     * Save a utilisateur.
     *
     * @param utilisateur the entity to save.
     * @return the persisted entity.
     */
    Utilisateur save(Utilisateur utilisateur);

    /**
     * Updates a utilisateur.
     *
     * @param utilisateur the entity to update.
     * @return the persisted entity.
     */
    Utilisateur update(Utilisateur utilisateur);

    /**
     * Partially updates a utilisateur.
     *
     * @param utilisateur the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Utilisateur> partialUpdate(Utilisateur utilisateur);

    /**
     * Get all the utilisateurs.
     *
     * @return the list of entities.
     */
    List<Utilisateur> findAll();

    /**
     * Get all the utilisateurs with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Utilisateur> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" utilisateur.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Utilisateur> findOne(Long id);

    /**
     * Delete the "id" utilisateur.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
