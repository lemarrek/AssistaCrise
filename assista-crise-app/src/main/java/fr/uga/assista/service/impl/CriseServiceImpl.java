package fr.uga.assista.service.impl;

import fr.uga.assista.domain.Crise;
import fr.uga.assista.repository.CriseRepository;
import fr.uga.assista.service.CriseService;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.uga.assista.domain.Crise}.
 */
@Service
@Transactional
public class CriseServiceImpl implements CriseService {

    private static final Logger LOG = LoggerFactory.getLogger(CriseServiceImpl.class);

    private final CriseRepository criseRepository;

    public CriseServiceImpl(CriseRepository criseRepository) {
        this.criseRepository = criseRepository;
    }

    @Override
    public Crise save(Crise crise) {
        LOG.debug("Request to save Crise : {}", crise);
        return criseRepository.save(crise);
    }

    @Override
    public Crise update(Crise crise) {
        LOG.debug("Request to update Crise : {}", crise);
        return criseRepository.save(crise);
    }

    @Override
    public Optional<Crise> partialUpdate(Crise crise) {
        LOG.debug("Request to partially update Crise : {}", crise);

        return criseRepository
            .findById(crise.getId())
            .map(existingCrise -> {
                updateIfPresent(existingCrise::setTitre, crise.getTitre());
                updateIfPresent(existingCrise::setTypeEvent, crise.getTypeEvent());
                updateIfPresent(existingCrise::setDateDebut, crise.getDateDebut());
                updateIfPresent(existingCrise::setDateFin, crise.getDateFin());
                updateIfPresent(existingCrise::setEstActive, crise.getEstActive());

                return existingCrise;
            })
            .map(criseRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Crise> findAll() {
        LOG.debug("Request to get all Crises");
        return criseRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Crise> findOne(Long id) {
        LOG.debug("Request to get Crise : {}", id);
        return criseRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Crise : {}", id);
        criseRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
