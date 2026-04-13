package fr.uga.assista.repository;

import fr.uga.assista.domain.Crise;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Crise entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CriseRepository extends JpaRepository<Crise, Long> {}
