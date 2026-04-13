package fr.uga.assista.service.impl;

import fr.uga.assista.domain.Information;
import fr.uga.assista.repository.InformationRepository;
import fr.uga.assista.service.InformationService;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.uga.assista.domain.Information}.
 */
@Service
@Transactional
public class InformationServiceImpl implements InformationService {

    private static final Logger LOG = LoggerFactory.getLogger(InformationServiceImpl.class);

    private final InformationRepository informationRepository;

    public InformationServiceImpl(InformationRepository informationRepository) {
        this.informationRepository = informationRepository;
    }

    @Override
    public Information save(Information information) {
        LOG.debug("Request to save Information : {}", information);
        return informationRepository.save(information);
    }

    @Override
    public Information update(Information information) {
        LOG.debug("Request to update Information : {}", information);
        return informationRepository.save(information);
    }

    @Override
    public Optional<Information> partialUpdate(Information information) {
        LOG.debug("Request to partially update Information : {}", information);

        return informationRepository
            .findById(information.getId())
            .map(existingInformation -> {
                updateIfPresent(existingInformation::setContenu, information.getContenu());
                updateIfPresent(existingInformation::setDatePublication, information.getDatePublication());

                return existingInformation;
            })
            .map(informationRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Information> findAll(Pageable pageable) {
        LOG.debug("Request to get all Informations");
        return informationRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Information> findOne(Long id) {
        LOG.debug("Request to get Information : {}", id);
        return informationRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Information : {}", id);
        informationRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
