package org.example.soutenanceservice.repository;

import org.example.soutenanceservice.entity.Soutenance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SoutenanceRepository extends JpaRepository<Soutenance, Long> {
}

