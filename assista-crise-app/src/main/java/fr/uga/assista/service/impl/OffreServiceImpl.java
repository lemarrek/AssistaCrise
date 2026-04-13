package fr.uga.assista.service.impl;

import fr.uga.assista.domain.Offre;
import fr.uga.assista.repository.OffreRepository;
import fr.uga.assista.service.OffreService;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.uga.assista.domain.Offre}.
 */
@Service
@Transactional
public class OffreServiceImpl implements OffreService {

    private static final Logger LOG = LoggerFactory.getLogger(OffreServiceImpl.class);

    private final OffreRepository offreRepository;

    public OffreServiceImpl(OffreRepository offreRepository) {
        this.offreRepository = offreRepository;
    }

    @Override
    public Offre save(Offre offre) {
        LOG.debug("Request to save Offre : {}", offre);
        return offreRepository.save(offre);
    }

    @Override
    public Offre update(Offre offre) {
        LOG.debug("Request to update Offre : {}", offre);
        return offreRepository.save(offre);
    }

    @Override
    public Optional<Offre> partialUpdate(Offre offre) {
        LOG.debug("Request to partially update Offre : {}", offre);

        return offreRepository
            .findById(offre.getId())
            .map(existingOffre -> {
                updateIfPresent(existingOffre::setTitre, offre.getTitre());
                updateIfPresent(existingOffre::setDescription, offre.getDescription());
                updateIfPresent(existingOffre::setServicePropose, offre.getServicePropose());
                updateIfPresent(existingOffre::setLatitude, offre.getLatitude());
                updateIfPresent(existingOffre::setLongitude, offre.getLongitude());
                updateIfPresent(existingOffre::setStatut, offre.getStatut());
                updateIfPresent(existingOffre::setDateCreation, offre.getDateCreation());

                return existingOffre;
            })
            .map(offreRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Offre> findAll(Pageable pageable) {
        LOG.debug("Request to get all Offres");
        return offreRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Offre> findOne(Long id) {
        LOG.debug("Request to get Offre : {}", id);
        return offreRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Offre : {}", id);
        offreRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
