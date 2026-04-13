package fr.uga.assista.service.impl;

import fr.uga.assista.domain.Utilisateur;
import fr.uga.assista.repository.UtilisateurRepository;
import fr.uga.assista.service.UtilisateurService;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.uga.assista.domain.Utilisateur}.
 */
@Service
@Transactional
public class UtilisateurServiceImpl implements UtilisateurService {

    private static final Logger LOG = LoggerFactory.getLogger(UtilisateurServiceImpl.class);

    private final UtilisateurRepository utilisateurRepository;

    public UtilisateurServiceImpl(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    @Override
    public Utilisateur save(Utilisateur utilisateur) {
        LOG.debug("Request to save Utilisateur : {}", utilisateur);
        return utilisateurRepository.save(utilisateur);
    }

    @Override
    public Utilisateur update(Utilisateur utilisateur) {
        LOG.debug("Request to update Utilisateur : {}", utilisateur);
        return utilisateurRepository.save(utilisateur);
    }

    @Override
    public Optional<Utilisateur> partialUpdate(Utilisateur utilisateur) {
        LOG.debug("Request to partially update Utilisateur : {}", utilisateur);

        return utilisateurRepository
            .findById(utilisateur.getId())
            .map(existingUtilisateur -> {
                updateIfPresent(existingUtilisateur::setNom, utilisateur.getNom());
                updateIfPresent(existingUtilisateur::setPrenom, utilisateur.getPrenom());
                updateIfPresent(existingUtilisateur::setEmail, utilisateur.getEmail());
                updateIfPresent(existingUtilisateur::setTelephone, utilisateur.getTelephone());
                updateIfPresent(existingUtilisateur::setRole, utilisateur.getRole());
                updateIfPresent(existingUtilisateur::setEstBanni, utilisateur.getEstBanni());

                return existingUtilisateur;
            })
            .map(utilisateurRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Utilisateur> findAll() {
        LOG.debug("Request to get all Utilisateurs");
        return utilisateurRepository.findAll();
    }

    public Page<Utilisateur> findAllWithEagerRelationships(Pageable pageable) {
        return utilisateurRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Utilisateur> findOne(Long id) {
        LOG.debug("Request to get Utilisateur : {}", id);
        return utilisateurRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Utilisateur : {}", id);
        utilisateurRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
