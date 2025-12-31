package org.example.inscriptionservice.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Candidat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;

    @OneToMany(mappedBy = "candidat")
    private List<Dossier> dossiers;

    @OneToMany
    private List<Publication> publications;

    @OneToMany
    private List<Formation> formations;

    public Long getId() {
        return id;
    }

    public List<Dossier> getDossiers() {
        return dossiers;
    }

    public void setDossiers(List<Dossier> dossiers) {
        this.dossiers = dossiers;
    }

    public List<Publication> getPublications() {
        return publications;
    }

    public void setPublications(List<Publication> publications) {
        this.publications = publications;
    }

    public List<Formation> getFormations() {
        return formations;
    }

    public void setFormations(List<Formation> formations) {
        this.formations = formations;
    }

    // ...autres getters/setters
}
