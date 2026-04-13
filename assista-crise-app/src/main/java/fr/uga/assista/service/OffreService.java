package fr.uga.assista.service;

import fr.uga.assista.domain.Offre;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link fr.uga.assista.domain.Offre}.
 */
public interface OffreService {
    /**
     * Save a offre.
     *
     * @param offre the entity to save.
     * @return the persisted entity.
     */
    Offre save(Offre offre);

    /**
     * Updates a offre.
     *
     * @param offre the entity to update.
     * @return the persisted entity.
     */
    Offre update(Offre offre);

    /**
     * Partially updates a offre.
     *
     * @param offre the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Offre> partialUpdate(Offre offre);

    /**
     * Get all the offres.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Offre> findAll(Pageable pageable);

    /**
     * Get the "id" offre.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Offre> findOne(Long id);

    /**
     * Delete the "id" offre.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
