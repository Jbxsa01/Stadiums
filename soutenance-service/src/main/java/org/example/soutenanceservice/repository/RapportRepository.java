package org.example.soutenanceservice.repository;

import org.example.soutenanceservice.entity.Rapport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RapportRepository extends JpaRepository<Rapport, Long> {
}

