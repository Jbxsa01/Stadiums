package org.example.inscriptionservice.entity;

import org.example.inscriptionservice.enums.StatutInscription;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "inscriptions")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Inscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long campagneId;

    @Column(nullable = false)
    private Long doctorantId;  // Référence vers users-service

    @Column(nullable = false)
    private Long directeurId;  // Référence vers users-service

    @Column(nullable = false)
    private String sujetThese;

    @Column(length = 2000)
    private String descriptionProjet;

    @Column(nullable = false)
    private String domaineRecherche;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutInscription statut = StatutInscription.BROUILLON;

    private String commentaireDirecteur;
    private LocalDateTime dateValidationDirecteur;

    private String commentaireAdmin;
    private LocalDateTime dateValidationAdmin;

    private LocalDateTime dateCreation = LocalDateTime.now();
    private LocalDateTime dateModification = LocalDateTime.now();
}
