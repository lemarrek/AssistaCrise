package fr.uga.assista.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fr.uga.assista.domain.enumeration.RoleUtilisateur;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Utilisateur.
 */
@Entity
@Table(name = "utilisateur")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Utilisateur implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 2, max = 50)
    @Column(name = "nom", length = 50, nullable = false)
    private String nom;

    @NotNull
    @Size(min = 2, max = 50)
    @Column(name = "prenom", length = 50, nullable = false)
    private String prenom;

    @NotNull
    @Pattern(regexp = "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")
    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "telephone")
    private String telephone;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private RoleUtilisateur role;

    @NotNull
    @Column(name = "est_banni", nullable = false)
    private Boolean estBanni;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_utilisateur__crises_touchees",
        joinColumns = @JoinColumn(name = "utilisateur_id"),
        inverseJoinColumns = @JoinColumn(name = "crises_touchees_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "validateur", "sinistreses" }, allowSetters = true)
    private Set<Crise> crisesToucheeses = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Utilisateur id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return this.nom;
    }

    public Utilisateur nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return this.prenom;
    }

    public Utilisateur prenom(String prenom) {
        this.setPrenom(prenom);
        return this;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return this.email;
    }

    public Utilisateur email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return this.telephone;
    }

    public Utilisateur telephone(String telephone) {
        this.setTelephone(telephone);
        return this;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public RoleUtilisateur getRole() {
        return this.role;
    }

    public Utilisateur role(RoleUtilisateur role) {
        this.setRole(role);
        return this;
    }

    public void setRole(RoleUtilisateur role) {
        this.role = role;
    }

    public Boolean getEstBanni() {
        return this.estBanni;
    }

    public Utilisateur estBanni(Boolean estBanni) {
        this.setEstBanni(estBanni);
        return this;
    }

    public void setEstBanni(Boolean estBanni) {
        this.estBanni = estBanni;
    }

    public Set<Crise> getCrisesToucheeses() {
        return this.crisesToucheeses;
    }

    public void setCrisesToucheeses(Set<Crise> crises) {
        this.crisesToucheeses = crises;
    }

    public Utilisateur crisesToucheeses(Set<Crise> crises) {
        this.setCrisesToucheeses(crises);
        return this;
    }

    public Utilisateur addCrisesTouchees(Crise crise) {
        this.crisesToucheeses.add(crise);
        return this;
    }

    public Utilisateur removeCrisesTouchees(Crise crise) {
        this.crisesToucheeses.remove(crise);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Utilisateur)) {
            return false;
        }
        return getId() != null && getId().equals(((Utilisateur) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Utilisateur{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", prenom='" + getPrenom() + "'" +
            ", email='" + getEmail() + "'" +
            ", telephone='" + getTelephone() + "'" +
            ", role='" + getRole() + "'" +
            ", estBanni='" + getEstBanni() + "'" +
            "}";
    }
}
