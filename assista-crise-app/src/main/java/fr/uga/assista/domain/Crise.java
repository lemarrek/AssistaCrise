package fr.uga.assista.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Crise.
 */
@Entity
@Table(name = "crise")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Crise implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "titre", nullable = false)
    private String titre;

    @NotNull
    @Column(name = "type_event", nullable = false)
    private String typeEvent;

    @NotNull
    @Column(name = "date_debut", nullable = false)
    private ZonedDateTime dateDebut;

    @Column(name = "date_fin")
    private ZonedDateTime dateFin;

    @NotNull
    @Column(name = "est_active", nullable = false)
    private Boolean estActive;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "crisesToucheeses" }, allowSetters = true)
    private Utilisateur validateur;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "crisesToucheeses")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "crisesToucheeses" }, allowSetters = true)
    private Set<Utilisateur> sinistreses = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Crise id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitre() {
        return this.titre;
    }

    public Crise titre(String titre) {
        this.setTitre(titre);
        return this;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getTypeEvent() {
        return this.typeEvent;
    }

    public Crise typeEvent(String typeEvent) {
        this.setTypeEvent(typeEvent);
        return this;
    }

    public void setTypeEvent(String typeEvent) {
        this.typeEvent = typeEvent;
    }

    public ZonedDateTime getDateDebut() {
        return this.dateDebut;
    }

    public Crise dateDebut(ZonedDateTime dateDebut) {
        this.setDateDebut(dateDebut);
        return this;
    }

    public void setDateDebut(ZonedDateTime dateDebut) {
        this.dateDebut = dateDebut;
    }

    public ZonedDateTime getDateFin() {
        return this.dateFin;
    }

    public Crise dateFin(ZonedDateTime dateFin) {
        this.setDateFin(dateFin);
        return this;
    }

    public void setDateFin(ZonedDateTime dateFin) {
        this.dateFin = dateFin;
    }

    public Boolean getEstActive() {
        return this.estActive;
    }

    public Crise estActive(Boolean estActive) {
        this.setEstActive(estActive);
        return this;
    }

    public void setEstActive(Boolean estActive) {
        this.estActive = estActive;
    }

    public Utilisateur getValidateur() {
        return this.validateur;
    }

    public void setValidateur(Utilisateur utilisateur) {
        this.validateur = utilisateur;
    }

    public Crise validateur(Utilisateur utilisateur) {
        this.setValidateur(utilisateur);
        return this;
    }

    public Set<Utilisateur> getSinistreses() {
        return this.sinistreses;
    }

    public void setSinistreses(Set<Utilisateur> utilisateurs) {
        if (this.sinistreses != null) {
            this.sinistreses.forEach(i -> i.removeCrisesTouchees(this));
        }
        if (utilisateurs != null) {
            utilisateurs.forEach(i -> i.addCrisesTouchees(this));
        }
        this.sinistreses = utilisateurs;
    }

    public Crise sinistreses(Set<Utilisateur> utilisateurs) {
        this.setSinistreses(utilisateurs);
        return this;
    }

    public Crise addSinistres(Utilisateur utilisateur) {
        this.sinistreses.add(utilisateur);
        utilisateur.getCrisesToucheeses().add(this);
        return this;
    }

    public Crise removeSinistres(Utilisateur utilisateur) {
        this.sinistreses.remove(utilisateur);
        utilisateur.getCrisesToucheeses().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Crise)) {
            return false;
        }
        return getId() != null && getId().equals(((Crise) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Crise{" +
            "id=" + getId() +
            ", titre='" + getTitre() + "'" +
            ", typeEvent='" + getTypeEvent() + "'" +
            ", dateDebut='" + getDateDebut() + "'" +
            ", dateFin='" + getDateFin() + "'" +
            ", estActive='" + getEstActive() + "'" +
            "}";
    }
}
