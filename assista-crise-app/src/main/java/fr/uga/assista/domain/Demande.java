package fr.uga.assista.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fr.uga.assista.domain.enumeration.StatutAnnonce;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Demande.
 */
@Entity
@Table(name = "demande")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Demande implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 5)
    @Column(name = "titre", nullable = false)
    private String titre;

    @Lob
    @Column(name = "description", nullable = false)
    private String description;

    @NotNull
    @Column(name = "type_besoin", nullable = false)
    private String typeBesoin;

    @NotNull
    @Column(name = "latitude", nullable = false)
    private Double latitude;

    @NotNull
    @Column(name = "longitude", nullable = false)
    private Double longitude;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "statut", nullable = false)
    private StatutAnnonce statut;

    @NotNull
    @Column(name = "date_creation", nullable = false)
    private ZonedDateTime dateCreation;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "crisesToucheeses" }, allowSetters = true)
    private Utilisateur auteur;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "validateur", "sinistreses" }, allowSetters = true)
    private Crise crise;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Demande id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitre() {
        return this.titre;
    }

    public Demande titre(String titre) {
        this.setTitre(titre);
        return this;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return this.description;
    }

    public Demande description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTypeBesoin() {
        return this.typeBesoin;
    }

    public Demande typeBesoin(String typeBesoin) {
        this.setTypeBesoin(typeBesoin);
        return this;
    }

    public void setTypeBesoin(String typeBesoin) {
        this.typeBesoin = typeBesoin;
    }

    public Double getLatitude() {
        return this.latitude;
    }

    public Demande latitude(Double latitude) {
        this.setLatitude(latitude);
        return this;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return this.longitude;
    }

    public Demande longitude(Double longitude) {
        this.setLongitude(longitude);
        return this;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public StatutAnnonce getStatut() {
        return this.statut;
    }

    public Demande statut(StatutAnnonce statut) {
        this.setStatut(statut);
        return this;
    }

    public void setStatut(StatutAnnonce statut) {
        this.statut = statut;
    }

    public ZonedDateTime getDateCreation() {
        return this.dateCreation;
    }

    public Demande dateCreation(ZonedDateTime dateCreation) {
        this.setDateCreation(dateCreation);
        return this;
    }

    public void setDateCreation(ZonedDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Utilisateur getAuteur() {
        return this.auteur;
    }

    public void setAuteur(Utilisateur utilisateur) {
        this.auteur = utilisateur;
    }

    public Demande auteur(Utilisateur utilisateur) {
        this.setAuteur(utilisateur);
        return this;
    }

    public Crise getCrise() {
        return this.crise;
    }

    public void setCrise(Crise crise) {
        this.crise = crise;
    }

    public Demande crise(Crise crise) {
        this.setCrise(crise);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Demande)) {
            return false;
        }
        return getId() != null && getId().equals(((Demande) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Demande{" +
            "id=" + getId() +
            ", titre='" + getTitre() + "'" +
            ", description='" + getDescription() + "'" +
            ", typeBesoin='" + getTypeBesoin() + "'" +
            ", latitude=" + getLatitude() +
            ", longitude=" + getLongitude() +
            ", statut='" + getStatut() + "'" +
            ", dateCreation='" + getDateCreation() + "'" +
            "}";
    }
}
