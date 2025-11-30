package org.example.soutenanceservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Jury {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Identifiant du membre du jury dans le microservice des utilisateurs
     * (users-service). Cela permet de récupérer ses infos détaillées via un appel REST/Feign.
     */
    private Long membreId;

    /** Nom complet du membre du jury, pour l'affichage/historisation. */
    private String nomComplet;

    /** Établissement du membre du jury (université, laboratoire, etc.). */
    private String etablissement;

    /**
     * Rôle du membre dans le jury : PRESIDENT, RAPPORTEUR, EXAMINATEUR, INVITE, etc.
     */
    private String role;

    /** Ordre d'affichage dans le jury (1 = président, 2 = rapporteur, ...). */
    private Integer ordre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "soutenance_id", nullable = false)
    private Soutenance soutenance;
}
