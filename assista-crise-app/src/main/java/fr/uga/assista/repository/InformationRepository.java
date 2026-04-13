package fr.uga.assista.repository;

import fr.uga.assista.domain.Information;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Information entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InformationRepository extends JpaRepository<Information, Long> {}
