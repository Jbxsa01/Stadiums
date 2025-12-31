package org.example.inscriptionservice.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Campagne {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private LocalDate dateOuverture;
    private LocalDate dateFermeture;
    private boolean active;
    // getters/setters
}
