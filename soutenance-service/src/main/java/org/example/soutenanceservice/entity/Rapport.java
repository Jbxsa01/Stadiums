package org.example.soutenanceservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Rapport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String avis;
    private String fichierUrl;

    @ManyToOne
    @JoinColumn(name = "soutenance_id")
    private Soutenance soutenance;
}
