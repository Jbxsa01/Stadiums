package org.example.inscriptionservice.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Dossier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String etat; // EN_ATTENTE, EN_COURS, VALIDE, REFUSE
    private String sujetThese;
    private String directeurThese;
    private String collaboration;
    @ElementCollection
    private List<String> pieces;
    @ManyToOne
    private Campagne campagne;
    @ManyToOne
    private Candidat candidat;
    // getters/setters
}
