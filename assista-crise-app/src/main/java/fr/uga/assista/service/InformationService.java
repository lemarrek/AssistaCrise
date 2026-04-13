package fr.uga.assista.service;

import fr.uga.assista.domain.Information;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link fr.uga.assista.domain.Information}.
 */
public interface InformationService {
    /**
     * Save a information.
     *
     * @param information the entity to save.
     * @return the persisted entity.
     */
    Information save(Information information);

    /**
     * Updates a information.
     *
     * @param information the entity to update.
     * @return the persisted entity.
     */
    Information update(Information information);

    /**
     * Partially updates a information.
     *
     * @param information the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Information> partialUpdate(Information information);

    /**
     * Get all the informations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Information> findAll(Pageable pageable);

    /**
     * Get the "id" information.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Information> findOne(Long id);

    /**
     * Delete the "id" information.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
