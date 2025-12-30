package org.example.soutenanceservice.repository;

import org.example.soutenanceservice.entity.Soutenance;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SoutenanceRepository extends JpaRepository<Soutenance, Long> {
    List<Soutenance> findByDoctorantId(Long doctorantId);
    List<Soutenance> findByStatut(String statut);
}
