package org.example.soutenanceservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Soutenance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long doctorantId;
    private LocalDateTime date;
    private String lieu;
    private String statut; // PLANIFIEE, EN_COURS, TERMINEE, ANNULEE

    @OneToMany(mappedBy = "soutenance", cascade = CascadeType.ALL)
    private List<Jury> jury;

    @OneToMany(mappedBy = "soutenance", cascade = CascadeType.ALL)
    private List<Rapport> rapports;
}
