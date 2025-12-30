package org.example.soutenanceservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "soutenances")
public class Soutenance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long doctorantId;

    private LocalDateTime dateSoutenance;
    private String lieu;

    @Column(nullable = false)
    private String statut; // EN_ATTENTE, VALIDEE, PLANIFIEE, TERMINEE, ANNULEE

    // Check-list de prérequis
    private Integer nombrePublications;
    private Integer creditsFormation;

    // Documents fournis
    private String demandeManuscrite;
    private String rapportThese;
    private String rapportAntiPlagiat;
    private String rapportPublications;
    private String attestationsFormations;
    private String autorisationSoutenance;

    // Validation administrative
    private Boolean checklistValidee = false;
    private LocalDateTime dateValidation;
    private Long validePar; // ID de l'admin qui a validé

    // Composition du jury
    @OneToMany(mappedBy = "soutenance", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Jury> jury = new ArrayList<>();

    // Rapports des membres du jury
    @OneToMany(mappedBy = "soutenance", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Rapport> rapports = new ArrayList<>();

    // Métadonnées
    @Column(nullable = false, updatable = false)
    private LocalDateTime dateCreation = LocalDateTime.now();

    private LocalDateTime dateModification;

    @PreUpdate
    protected void onUpdate() {
        dateModification = LocalDateTime.now();
    }
}
