package fr.uga.assista.service;

import fr.uga.assista.domain.Crise;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link fr.uga.assista.domain.Crise}.
 */
public interface CriseService {
    /**
     * Save a crise.
     *
     * @param crise the entity to save.
     * @return the persisted entity.
     */
    Crise save(Crise crise);

    /**
     * Updates a crise.
     *
     * @param crise the entity to update.
     * @return the persisted entity.
     */
    Crise update(Crise crise);

    /**
     * Partially updates a crise.
     *
     * @param crise the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Crise> partialUpdate(Crise crise);

    /**
     * Get all the crises.
     *
     * @return the list of entities.
     */
    List<Crise> findAll();

    /**
     * Get the "id" crise.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Crise> findOne(Long id);

    /**
     * Delete the "id" crise.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
