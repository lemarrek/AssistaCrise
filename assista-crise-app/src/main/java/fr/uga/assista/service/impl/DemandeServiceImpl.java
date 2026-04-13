package fr.uga.assista.service.impl;

import fr.uga.assista.domain.Demande;
import fr.uga.assista.repository.DemandeRepository;
import fr.uga.assista.service.DemandeService;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.uga.assista.domain.Demande}.
 */
@Service
@Transactional
public class DemandeServiceImpl implements DemandeService {

    private static final Logger LOG = LoggerFactory.getLogger(DemandeServiceImpl.class);

    private final DemandeRepository demandeRepository;

    public DemandeServiceImpl(DemandeRepository demandeRepository) {
        this.demandeRepository = demandeRepository;
    }

    @Override
    public Demande save(Demande demande) {
        LOG.debug("Request to save Demande : {}", demande);
        return demandeRepository.save(demande);
    }

    @Override
    public Demande update(Demande demande) {
        LOG.debug("Request to update Demande : {}", demande);
        return demandeRepository.save(demande);
    }

    @Override
    public Optional<Demande> partialUpdate(Demande demande) {
        LOG.debug("Request to partially update Demande : {}", demande);

        return demandeRepository
            .findById(demande.getId())
            .map(existingDemande -> {
                updateIfPresent(existingDemande::setTitre, demande.getTitre());
                updateIfPresent(existingDemande::setDescription, demande.getDescription());
                updateIfPresent(existingDemande::setTypeBesoin, demande.getTypeBesoin());
                updateIfPresent(existingDemande::setLatitude, demande.getLatitude());
                updateIfPresent(existingDemande::setLongitude, demande.getLongitude());
                updateIfPresent(existingDemande::setStatut, demande.getStatut());
                updateIfPresent(existingDemande::setDateCreation, demande.getDateCreation());

                return existingDemande;
            })
            .map(demandeRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Demande> findAll(Pageable pageable) {
        LOG.debug("Request to get all Demandes");
        return demandeRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Demande> findOne(Long id) {
        LOG.debug("Request to get Demande : {}", id);
        return demandeRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Demande : {}", id);
        demandeRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
