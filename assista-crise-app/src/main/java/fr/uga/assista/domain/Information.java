package fr.uga.assista.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Information.
 */
@Entity
@Table(name = "information")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Information implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Lob
    @Column(name = "contenu", nullable = false)
    private String contenu;

    @NotNull
    @Column(name = "date_publication", nullable = false)
    private ZonedDateTime datePublication;

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

    public Information id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContenu() {
        return this.contenu;
    }

    public Information contenu(String contenu) {
        this.setContenu(contenu);
        return this;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public ZonedDateTime getDatePublication() {
        return this.datePublication;
    }

    public Information datePublication(ZonedDateTime datePublication) {
        this.setDatePublication(datePublication);
        return this;
    }

    public void setDatePublication(ZonedDateTime datePublication) {
        this.datePublication = datePublication;
    }

    public Utilisateur getAuteur() {
        return this.auteur;
    }

    public void setAuteur(Utilisateur utilisateur) {
        this.auteur = utilisateur;
    }

    public Information auteur(Utilisateur utilisateur) {
        this.setAuteur(utilisateur);
        return this;
    }

    public Crise getCrise() {
        return this.crise;
    }

    public void setCrise(Crise crise) {
        this.crise = crise;
    }

    public Information crise(Crise crise) {
        this.setCrise(crise);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Information)) {
            return false;
        }
        return getId() != null && getId().equals(((Information) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Information{" +
            "id=" + getId() +
            ", contenu='" + getContenu() + "'" +
            ", datePublication='" + getDatePublication() + "'" +
            "}";
    }
}
